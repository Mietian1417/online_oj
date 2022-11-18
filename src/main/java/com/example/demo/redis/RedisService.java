package com.example.demo.redis;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 该类封装了 redis 的操作
 */
@Service
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 获取当个对象
     */
    public <T> T get(PrefixKey prefix, String key, Class c) {
        //生成真正的key
        String realKey = prefix.getPrefixKey() + key;
        return (T) redisTemplate.opsForValue().get(realKey);
    }

    public void expire(PrefixKey prefix, String key, int exTime) {
        String realKey = prefix.getPrefixKey() + key;
        redisTemplate.expire(realKey, exTime, TimeUnit.SECONDS);
    }

    /**
     * 设置对象
     */
    public <T> Boolean set(PrefixKey prefix, String key, T value, int exTime) {
        // 调用父类 BasePrefix 的 getPrefix() 方法, 拼接 key,
        String realKey = prefix.getPrefixKey() + key;
        try {
            if (exTime > 0) {
                redisTemplate.opsForValue().set(realKey, value, exTime, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(realKey, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Boolean del(PrefixKey prefix, String key) {
        String realKey = prefix.getPrefixKey() + key;
        return redisTemplate.delete(realKey);
    }

    /**
     * 判断key是否存在
     */
    public <T> Boolean exists(PrefixKey prefix, String key) {
        String realKey = prefix.getPrefixKey() + key;
        return redisTemplate.hasKey(realKey);
    }


    /**
     * bean 转 String
     *
     * @param value
     * @param <T>
     * @return
     */
    public static <T> String beanToString(T value) {
        if (value == null) {
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz == Integer.class) {
            return "" + value;
        } else if (clazz == String.class) {
            return (String) value;
        } else if (clazz == long.class || clazz == Long.class) {
            return "" + value;
        } else {
            return JSON.toJSONString(value);
        }
    }


    /**
     * string转bean
     *
     * @param str
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T stringToBean(String str, Class<T> clazz) {
        if (str == null || str.length() <= 0 || clazz == null) {
            return null;
        }
        if (clazz == int.class || clazz == Integer.class) {
            return (T) Integer.valueOf(str);
        } else if (clazz == String.class) {
            return (T) str;
        } else if (clazz == long.class || clazz == Long.class) {
            return (T) Long.valueOf(str);
        } else {
            return JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

}
