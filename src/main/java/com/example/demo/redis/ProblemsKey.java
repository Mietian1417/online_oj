package com.example.demo.redis;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-18
 * Time: 22:10
 *
 * @author 陈子豪
 */
public class ProblemsKey extends BasePrefix{
    public ProblemsKey(String prefix) {
        super(prefix);
    }
    public static ProblemsKey getProblems = new ProblemsKey("problems");
    public static ProblemsKey getProblemsCount = new ProblemsKey("problemsCount");
    public static ProblemsKey getProblemsOfPagesCount = new ProblemsKey("pagesCount");
    public static ProblemsKey getProblemsOfPage = new ProblemsKey("problemListOfPage");

    public static ProblemsKey getCurPageProblemsIsPassOfUser(int page, int userId){
        return  new ProblemsKey("pageProblemsIsPassOfUser:" + "page:" + page + ",userId:" + userId);
    }
}
