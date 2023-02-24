package com.example.demo.controller;

import com.example.demo.model.User;
import com.example.demo.param.GroupSeq;
import com.example.demo.param.UserParam;
import com.example.demo.redis.ProblemsKey;
import com.example.demo.redis.RedisCacheTime;
import com.example.demo.redis.UserKey;
import com.example.demo.result.ErrorCode;
import com.example.demo.result.Result;
import com.example.demo.service.RedisService;
import com.example.demo.service.UserService;
import com.example.demo.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-12-05
 * Time: 8:59
 *
 * @author 陈子豪
 */

@RestController
public class RegisterController {

    @Autowired
    private UserService userService;

    @Autowired
    private RedisService redisService;

    @Autowired
    private ExecutorService executorService;

    @RequestMapping("/register")
    public Result<String> register(@Validated({GroupSeq.class}) UserParam userParam) {

        List<User> userList = redisService.get(UserKey.getAllUsers, "", List.class);
        for (User user : userList) {
            if (user.getUsername().equals(userParam.getUsername())) {
                return Result.error(ErrorCode.USER_EXISTS);
            }
        }

        User addUser = new User();
        addUser.setUsername(userParam.getUsername());
        // 密码加密
        String passwordToMad5 = MD5Util.getMD5(userParam.getPassword());
        addUser.setPassword(passwordToMad5);

        int isSuccess = userService.addUser(addUser);
        if (isSuccess == 0) {
            return Result.error(ErrorCode.USER_ADD_FAIL);
        }

        // 添加用户到缓存
        userList.add(addUser);
        redisService.set(UserKey.getAllUsers, "", userList, RedisCacheTime.NERVER_EXPIRES);

        // 用新的线程去更新缓存(新的用户通过状态, 默认全未通过), 不要耽误用户时间
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                int pagesCount = redisService.get(ProblemsKey.getProblemsOfPagesCount, "", Integer.class);
                int problemsCount = redisService.get(ProblemsKey.getProblemsCount, "", Integer.class);
                ArrayList<Integer> isPassList = new ArrayList<>(10);
                for (int i = 1; i < pagesCount; i++) {
                    isPassList.addAll(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0, 0));
                    redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(i, addUser.getId()), "", isPassList, RedisCacheTime.NERVER_EXPIRES);
                    isPassList.clear();
                }
                int lastPageProblem = problemsCount % 10;
                if (problemsCount % 10 == 0 && problemsCount != 0) {
                    lastPageProblem = 10;
                }
                Integer[] lastPageIsPassList = new Integer[lastPageProblem];
                for (int i = 0; i < lastPageProblem; i++) {
                    lastPageIsPassList[i] = 0;
                }
                isPassList.addAll(Arrays.asList(lastPageIsPassList));
                redisService.set(ProblemsKey.getCurPageProblemsIsPassOfUser(pagesCount, addUser.getId()), "", lastPageIsPassList, RedisCacheTime.NERVER_EXPIRES);
            }
        });
        return Result.success("");
    }
}
