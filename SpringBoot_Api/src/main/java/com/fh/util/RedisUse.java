package com.fh.util;

import redis.clients.jedis.Jedis;

import java.util.List;

public class RedisUse {

    public static void set(String key,String value){
        Jedis jedis = RedisUtl.getJedis();
        jedis.set(key,value);
        RedisUtl.returnJedis(jedis);
    }

    public static void set(String key,String value,int seconds){
        Jedis jedis = RedisUtl.getJedis();
        jedis.setex(key,seconds,value);
        RedisUtl.returnJedis(jedis);
    }

    public static String get(String key){
        Jedis jedis = RedisUtl.getJedis();
        String value=jedis.get(key);
        RedisUtl.returnJedis(jedis);
        return value;
    }


    public static long hlen(String key){
        Jedis jedis = RedisUtl.getJedis();
        Long hlen = jedis.hlen(key);
        RedisUtl.returnJedis(jedis);
        return hlen;
    }


    public static void hset(String key,String filed,String value){
        Jedis jedis = RedisUtl.getJedis();
        Long hset = jedis.hset(key, filed, value);
        RedisUtl.returnJedis(jedis);
    }


    public static String hget(String key,String filed){
        Jedis jedis=RedisUtl.getJedis();
        String hegt=jedis.hget(key,filed);
        RedisUtl.returnJedis(jedis);
        return hegt;
    }

    public static List<String> hvals(String key){
        Jedis jedis = RedisUtl.getJedis();
        List<String> hvals = jedis.hvals(key);
        RedisUtl.returnJedis(jedis);
        return hvals;
    }

    public static void hdel(String key,String filed){
        Jedis jedis=RedisUtl.getJedis();
        jedis.hdel(key,filed);
        RedisUtl.returnJedis(jedis);
    }


    public static boolean exists(String key){
        Jedis jedis = RedisUtl.getJedis();
        Boolean exists = jedis.exists(key);
        RedisUtl.returnJedis(jedis);
        return exists;
    }
    
    public static void hgetAll(String key){
        Jedis jedis=RedisUtl.getJedis();
        jedis.hgetAll(key);
        RedisUtl.returnJedis(jedis);
    }
}
