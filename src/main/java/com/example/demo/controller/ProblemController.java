package com.example.demo.controller;

import com.example.demo.model.Answer;
import com.example.demo.model.Problem;
import com.example.demo.model.User;
import com.example.demo.param.GroupSeq;
import com.example.demo.param.ProblemParam;
import com.example.demo.redis.ProblemKey;
import com.example.demo.redis.ProblemsKey;
import com.example.demo.redis.RedisCacheTime;
import com.example.demo.redis.UserKey;
import com.example.demo.result.ErrorCode;
import com.example.demo.result.Result;
import com.example.demo.service.ProblemService;
import com.example.demo.service.RedisService;
import com.example.demo.service.UserService;
import com.example.demo.vo.ViewProblems;
import com.example.demo.vo.viewProbelm;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.annotation.PostConstruct;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User: 陈子豪
 * Date: 2022-05-06
 * Time: 9:36
 */

/**
 * @Validated 开启 controller 类下方法的参数校验
 */
@RequestMapping("/online_oj")
@RestController
@Validated
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    /**
     * 初始化, 将所有的详情题目加载到缓存中, 将题目列表加入缓存, 将所有用户题目的通过状态加入缓存
     */
    @PostConstruct
    public void loadProblemsToRedis() {
        // 缓存题目列表页上的展示题目(id, 标题, 难度)
        List<Problem> problemList = problemService.getProblemList();
        for (Problem simpleProblem : problemList) {
            Problem problem = problemService.getProblemById(simpleProblem.getId());
            redisService.set(ProblemKey.getProblem, problem.getId() + "", problem, RedisCacheTime.DETAIL_PROBLEM_CACHE_TIME);
        }
        redisService.set(ProblemsKey.getProblems, "", problemList, RedisCacheTime.NERVER_EXPIRES);

        // 每个用户要遍历所有题目的通过情况, 并且这些题目还要分页缓存
        List<User> userList = userService.getAllUsers();
        // 将所有用户存入 redis 缓存
        redisService.set(UserKey.getAllUsers, "", userList, RedisCacheTime.NERVER_EXPIRES);

        List<Integer> curPageIsPassOfUser = new ArrayList<>();
        for (User user : userList) {
            int page = 1;
            for (int i = 0, count = 1; i < problemList.size(); i++, count++) {
                Integer isPass = userService.isPass(user.getId(), problemList.get(i).getId());
                if (isPass == null) {
                    isPass = 0;
                }
                curPageIsPassOfUser.add(isPass);
                // 存取当前一页用户通过的情况, 由于 sql 过多, 直接设置永不过期
                if (count % 10 == 0) {
                    redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(page, user.getId()), "",
                            curPageIsPassOfUser, RedisCacheTime.NERVER_EXPIRES);
                    curPageIsPassOfUser.clear();
                    page++;
                }
            }
            // 每个用户遍历完了题目, 可能没有到分页的设定值
            redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(page, user.getId()), "",
                    curPageIsPassOfUser, RedisCacheTime.NERVER_EXPIRES);
            curPageIsPassOfUser.clear();
        }
    }

    @RequestMapping("/list")
    public Result<ViewProblems> getProblemList(@SessionAttribute("user") User user, Integer startPage) {

        // 先查看 redis 缓存是否存在该页
        List<Problem> problems = redisService.get(ProblemsKey.getProblemsOfPage, startPage + "", List.class);
        if (problems != null) {
            // 获取题目通过状态
            List<Integer> userIsPassList =
                    redisService.get(ProblemsKey.getCurPageProblemsIsPassOfUser(startPage, user.getId()),
                            "", List.class);

            for (int i = 0; i < problems.size(); i++) {
                problems.get(i).setIsPass(userIsPassList.get(i));
            }
            // 创建返回对象
            ViewProblems viewProblems = new ViewProblems();
            viewProblems.setIsAdmin(user.getIsAdmin());
            viewProblems.setProblemList(problems);
            viewProblems.setStartPage(startPage);
            int lastPage = redisService.get(ProblemsKey.getProblemsOfPagesCount, "", Integer.class);
            int isLastPage = lastPage == startPage ? 1 : 0;
            viewProblems.setIsLastPage(isLastPage);
            return Result.success(viewProblems);
        }


        // 第几页, 每页个数, 查询排序方式
        // (该语句会拦截下行的 sql 语句, 进行 sql 拼接)
        PageHelper.startPage(startPage, 10, "id asc");
        List<Problem> problemList = problemService.getProblemList();
        PageInfo<Problem> pages = new PageInfo<>(problemList);

        // 所有数据条数缓存
        redisService.set(ProblemsKey.getProblemsCount, "", (int) pages.getTotal(), RedisCacheTime.NERVER_EXPIRES);
        // 数据页数缓存
        redisService.set(ProblemsKey.getProblemsOfPagesCount, "", pages.getPages(), RedisCacheTime.NERVER_EXPIRES);
        // 设置这一页的 redis 缓存
        redisService.set(ProblemsKey.getProblemsOfPage, startPage + "", problemList, RedisCacheTime.PROBLEMS_CACHE_TIME);

        // 获取题目通过状态
        List<Integer> userIsPassList = redisService.get(ProblemsKey.getCurPageProblemsIsPassOfUser(startPage, user.getId()), "", List.class);
        for (int i = 0; i < problemList.size(); i++) {
            problemList.get(i).setIsPass(userIsPassList.get(i));
        }
        // 创建返回对象
        ViewProblems viewProblems = new ViewProblems();
        viewProblems.setIsAdmin(user.getIsAdmin());
        viewProblems.setProblemList(problemList);
        viewProblems.setStartPage(startPage);
        int lastPage = redisService.get(ProblemsKey.getProblemsOfPagesCount, "", Integer.class);
        int isLastPage = lastPage == startPage ? 1 : 0;
        viewProblems.setIsLastPage(isLastPage);
        return Result.success(viewProblems);
    }

    @RequestMapping("/detail")
    public Result<viewProbelm> getDetail(@Min(value = 1, message = ("题目 id 不存在! "))
                                         @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                         @SessionAttribute("user") User user) {

        // 查看 redis 是否存在, 存在直接返回
        Problem redisProblem = redisService.get(ProblemKey.getProblem, problemId + "", Problem.class);
        if (redisProblem != null) {
            viewProbelm viewProbelm = new viewProbelm();
            viewProbelm.setIsAdmin(user.getIsAdmin());
            viewProbelm.setProblem(redisProblem);
            return Result.success(viewProbelm);
        }

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        viewProbelm viewProbelm = new viewProbelm();
        viewProbelm.setIsAdmin(user.getIsAdmin());
        viewProbelm.setProblem(problem);
        // redis 不存在, 建立缓存
        redisService.set(ProblemKey.getProblem, problemId + "", problem, RedisCacheTime.DETAIL_PROBLEM_CACHE_TIME);
        return Result.success(viewProbelm);
    }


    @RequestMapping("/submit")
    public Result<Answer> submitCode(@Min(value = 1, message = ("题目 id 不存在! "))
                                     @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                     @RequestParam("code") String code,
                                     @Min(value = 1, message = ("页码不存在! ")) Integer page,
                                     @Min(value = 1, message = ("题目排名不存在! "))
                                     @Max(value = 10, message = ("题目排名不存在! ")) Integer position,
                                     @SessionAttribute("user") User user) {

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        // 获取题目的测试代码, 拼接提交代码进行测试
        String testCode = problem.getTestCode();
        Answer answer = problemService.submitAndSaveCode(user.getId(), problemId, testCode, code);

        // 用户提交代码通过全部用例, 标记题目为通过状态
        String stdout = answer.getStdout();
        if (stdout != null) {
            // 全部通过返回 1, 未全部通过返回 0, SQL 出错 返回 -1
            int status = userService.updateUserOfProblemIsPass(stdout, user.getId(), problemId);
            if (status == -1) {
                return Result.error(ErrorCode.SERVER_EXECUTE_CODE_FAIL);
            }
            // 全部通过, 更新该用户题目通过状态的缓存
            if (status == 1) {
                List<Integer> isPassList = redisService.get(ProblemsKey.getCurPageProblemsIsPassOfUser(page, user.getId()), "", List.class);
                for (int i = 0; i < isPassList.size(); i++) {
                    if (i == (position - 1)) {
                        isPassList.set(i, 1);
                        break;
                    }
                }
                userService.setPass(user.getId(), position);
                redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(page, user.getId()), "", isPassList, RedisCacheTime.NERVER_EXPIRES);
            }
        }
        return Result.success(answer);
    }

    @RequestMapping("/loadLastSubmitCode")
    public Result<String> getLastSubmitCode(@Min(value = 1, message = ("题目 id 不存在! "))
                                            @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                            @SessionAttribute("user") User user) {

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        String lastSubmitCode = userService.getLastSubmitCode(problemId, user.getId());
        if (lastSubmitCode == null) {
            return Result.error(ErrorCode.USER_NOT_SUBMIT_ANY_CODE);
        }
        return Result.success(lastSubmitCode);
    }

    @RequestMapping("/loadReferenceAnswer")
    public Result<String> getReferenceAnswer(@Min(value = 1, message = ("题目 id 不存在! "))
                                             @NotNull(message = "题目 id 不存在! ") Integer problemId) {

        // 查看 redis 题目缓存是否存在, 存在直接返回
        Problem redisProblem = redisService.get(ProblemKey.getProblem, problemId + "", Problem.class);
        if (redisProblem != null) {
            return Result.success(redisProblem.getReferenceCode());
        }

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }
        return Result.success(problem.getReferenceCode());
    }


    @RequestMapping("/addProblem")
    public Result<Integer> addProblem(@Validated({GroupSeq.class}) ProblemParam problemParam) {

        // 获取新增题目的 id, 由 problemParam 对象的 id 属性接收
        int isSuccess = problemService.addProblem(problemParam);
        if (isSuccess == 0) {
            return Result.error(ErrorCode.PROBLEM_ADD_FAIL);
        }

        // 得到所有用户, 后续更新所有用户最后一页的题目通过状态
        List<User> userList = redisService.get(UserKey.getAllUsers, "", List.class);


        // 缓存题目总数量加 1
        redisService.incr(ProblemsKey.getProblemsCount, "");
        // 如果缓存中的题目数量模上 10 还余 1, 那么要新建一页
        // 查看题目总页数
        int problemsCount = redisService.get(ProblemsKey.getProblemsCount, "", Integer.class);
        if (problemsCount % 10 == 1) {
            redisService.incr(ProblemsKey.getProblemsOfPagesCount, "");
        }

        // 创建 redis 存储对象(其实直接用 problemParam 对象也可以, 但是取出的时候就要做类型判断)
        // 题目列表只有 id, 标题, 难度
        Problem problem = new Problem();
        problem.setId(problemParam.getId());
        problem.setTitle(problemParam.getTitle());
        problem.setLevel(problemParam.getLevel());

        // 更新总题目列表缓存
        List<Problem> updateProblems = redisService.get(ProblemsKey.getProblems, "", List.class);
        updateProblems.add(problem);
        redisService.set(ProblemsKey.getProblems, "", updateProblems, RedisCacheTime.PROBLEMS_CACHE_TIME);

        // 更新 redis 缓存列表
        // 得到最后一页的页数
        int lastPage = redisService.get(ProblemsKey.getProblemsOfPagesCount, "", Integer.class);
        if (problemsCount % 10 == 1) {
            // 必须再建一页, 这个时候必定没有该页的缓存, 建立该页的缓存
            List<Problem> newProblemsList = new ArrayList<>();
            newProblemsList.add(problem);
            redisService.set(ProblemsKey.getProblemsOfPage, lastPage + "", newProblemsList, RedisCacheTime.PROBLEMS_CACHE_TIME);

            // TODO 添加所有用户的最后一页题目的通过情况
            for (User user : userList) {
                List<Integer> isPassList = new ArrayList<>();
                isPassList.add(0);
                redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(lastPage, user.getId()), "",
                        isPassList, RedisCacheTime.NERVER_EXPIRES);
            }

        } else {
            // 不要再建一页, 更新该页缓存即可
            // 从 redis 查看最后一页
            List<Problem> problems = redisService.get(ProblemsKey.getProblemsOfPage, lastPage + "", List.class);
            // 缓存中还没有最后一页
            if (problems == null) {
                PageHelper.startPage(lastPage, 10, "id asc");
                List<Problem> problemList = problemService.getProblemList();
                PageInfo<Problem> pages = new PageInfo<>(problemList);
                problemList = pages.getList();
                // 建立这一页的缓存
                redisService.set(ProblemsKey.getProblemsOfPage, lastPage + "", problemList, RedisCacheTime.PROBLEMS_CACHE_TIME);
            } else {
                // 缓存存在最后一页, 更新本页缓存
                problems.add(problem);
                redisService.set(ProblemsKey.getProblemsOfPage, lastPage + "", problems, RedisCacheTime.PROBLEMS_CACHE_TIME);
            }

            // TODO 更新最后一页的题目通过状态
            for (User user : userList) {
                List<Integer> isPassList = redisService.get(ProblemsKey.getCurPageProblemsIsPassOfUser(lastPage, user.getId()), "", List.class);
                isPassList.add(0);
                redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(lastPage, user.getId()), "",
                        isPassList, RedisCacheTime.NERVER_EXPIRES);
            }
        }

        problem.setDescription(problemParam.getDescription());
        problem.setTestCode(problemParam.getTestCode());
        problem.setReferenceCode(problemParam.getReferenceCode());
        problem.setTemplateCode(problemParam.getTemplateCode());
        // 将该题目添加到 redis 题目缓存
        redisService.set(ProblemKey.getProblem, problem.getId() + "", problem, RedisCacheTime.DETAIL_PROBLEM_CACHE_TIME);
        // 返回最后一页的页数
        return Result.success(lastPage);
    }

    @RequestMapping("/getAllProblems")
    public Result<List<Problem>> getAllProblems() {
        List<Problem> problemList = redisService.get(ProblemsKey.getProblems, "", List.class);
        return Result.success(problemList);
    }


    @RequestMapping("/deleteProblem")
    public Result<Integer> deleteProblem(@Min(value = 1, message = ("题目 id 不存在! "))
                                         @NotNull(message = "题目 id 不存在! ") Integer problemId,
                                         String problemPosition) {

        Problem problem = problemService.getProblemById(problemId);
        if (problem == null) {
            return Result.error(ErrorCode.PROBLEM_IS_NOT_EXISTS);
        }

        Integer isSuccess = problemService.deleteProblemById(problemId);
        if (isSuccess == null || isSuccess == 0) {
            return Result.error(ErrorCode.PROBLEM_DELETE_FAIL);
        }

        // 删除所有用户在这题的提交代码和通过状态
        List<User> userList = redisService.get(UserKey.getAllUsers, "", List.class);
        // 这里可能有的用户并没有做过这道题, 查询
        for (User user : userList) {
            userService.deleteUserSubmitCode(user.getId(), problemId);
        }


        // 从 redis 中删除该题目的缓存(无论缓存是否存在, 该方法都不会报错, 只是返回一个是否删除成功的状态)
        redisService.del(ProblemKey.getProblem, problem.getId() + "");
        // 缓存题目数量减 1
        redisService.decr(ProblemsKey.getProblemsCount, "");
        // 缓存页数判断是否要减 1
        int problemsCount = redisService.get(ProblemsKey.getProblemsCount, "", Integer.class);
        if (problemsCount % 10 == 0) {
            redisService.decr(ProblemsKey.getProblemsOfPagesCount, "");
        }

        // 更新所有题目列表缓存
        List<Problem> problems = redisService.get(ProblemsKey.getProblems, "", List.class);
        for (int i = 0; i < problems.size(); i++) {
            if (problems.get(i).getId() == problemId) {
                problems.remove(i);
                break;
            }
        }
        redisService.set(ProblemsKey.getProblems, "", problems, RedisCacheTime.PROBLEMS_CACHE_TIME);

        // 更新 redis 题目每页列表缓存
        // 得到题目位置
        int problemRank = Integer.parseInt(problemPosition);
        // 得到删除题目所在的页数
        int deleteProblemOfPage = problemRank / 10 + 1;
        if (problemRank % 10 == 0) {
            deleteProblemOfPage -= 1;
        }
        // 更新该题目所在页以及后面全部页的缓存
        int pagesCount = redisService.get(ProblemsKey.getProblemsOfPagesCount, "", Integer.class);
        for (int i = deleteProblemOfPage; i <= pagesCount; i++) {
            PageHelper.startPage(i, 10);
            List<Problem> problemList = problemService.getProblemList();
            redisService.set(ProblemsKey.getProblemsOfPage, i + "", problemList, RedisCacheTime.PROBLEMS_CACHE_TIME);
        }

        // 更新所有用户该页以及该页后的所有题目通过状态的缓存
        List<Problem> problemList = redisService.get(ProblemsKey.getProblems, "", List.class);
        // 得到需要更新的题目列表
        problemList = problemList.subList((deleteProblemOfPage - 1) * 10, problemList.size());
        List<Integer> isPassList = new ArrayList<>();
        for (User user : userList) {
            int page = deleteProblemOfPage;
            for (int i = 0, count = 1; i < problemList.size(); i++, count++) {
                Integer isPass = userService.isPass(user.getId(), problemList.get(i).getId());
                if (isPass == null) {
                    isPass = 0;
                }
                isPassList.add(isPass);
                if (count % 10 == 0) {
                    redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(page, user.getId()), "",
                            isPassList, RedisCacheTime.NERVER_EXPIRES);
                    isPassList.clear();
                    page++;
                }
            }
            // 每个用户遍历完了题目, 可能没有到分页的设定值
            redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(page, user.getId()), "",
                    isPassList, RedisCacheTime.NERVER_EXPIRES);
            isPassList.clear();
        }

        // 删除每个用户多余页的通过状态
        if (problemsCount % 10 == 0) {
            for (User user : userList) {
                redisService.del(ProblemsKey.getCurPageProblemsIsPassOfUser(pagesCount + 1, user.getId()), "");
            }
        }

        // 删除多余页的缓存
        redisService.del(ProblemsKey.getProblemsOfPage, pagesCount + 1 + "");
        return Result.success(isSuccess);
    }
}
