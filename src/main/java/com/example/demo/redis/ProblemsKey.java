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
}
