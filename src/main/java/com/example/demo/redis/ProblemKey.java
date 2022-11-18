package com.example.demo.redis;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-18
 * Time: 22:53
 *
 * @author 陈子豪
 */
public class ProblemKey extends BasePrefix {
    public ProblemKey(String prefix) {
        super(prefix);
    }

    public static ProblemKey getProblem = new ProblemKey("problem");
}
