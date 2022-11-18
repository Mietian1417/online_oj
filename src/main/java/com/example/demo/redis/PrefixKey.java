package com.example.demo.redis;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-18
 * Time: 22:02
 *
 * @author 陈子豪
 */

/**
 * redis 拼接前缀 key
 *      用来区分不同的 key
 */
public interface PrefixKey {
    String getPrefixKey();
}
