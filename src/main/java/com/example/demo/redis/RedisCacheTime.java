package com.example.demo.redis;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-18
 * Time: 22:31
 *
 * @author 陈子豪
 */

/**
 * 对象在 redis 中的缓存时间
 */
public class RedisCacheTime {
    // 题目列表 -> 1天
    public static final int PROBLEMS_CACHE_TIME = 24 * 60 * 60;

    // 具体题目 -> 30分钟
    public static final int DETAIL_PROBLEM_CACHE_TIME = 30 * 60;

    // 永不过期
    public static final int NERVER_EXPIRES = -1;
}
