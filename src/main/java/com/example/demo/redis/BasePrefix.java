package com.example.demo.redis;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * Date: 2022-11-18
 * Time: 22:03
 *
 * @author 陈子豪
 */

/**
 * 抽象类实现 PrefixKey 接口
 *      基本前缀 key 为 对应的类名 + 指定前缀
 */
public abstract class BasePrefix implements PrefixKey {

    private String prefix;

    public BasePrefix(String prefix) {
        this.prefix = prefix;
    }

    // 前缀 key 为 具体调用者的类名 + 指定前缀
    @Override
    public String getPrefixKey() {
        String className = getClass().getSimpleName();
        return className+":" + prefix;
    }
}
