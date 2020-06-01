package com.ljw.springcloud.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.Resource;

/**
 * @Description:
 * @Author: Ljw
 * @Date: 2020/5/13.
 */
public class PaymentService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    public void redisTest(){
        // 获取连接池配置对象
        JedisPoolConfig config = new JedisPoolConfig();
        // 设置最大连接数
        config.setMaxTotal(30);
        // 设置最大的空闲连接数
        config.setMaxIdle(10);

        // 获得连接池: JedisPool jedisPool = new JedisPool(poolConfig,host,port);
        JedisPool jedisPool = new JedisPool(config,"193.112.79.192",6379);

        // 存入永久数据
//        stringRedisTemplate.opsForValue().set("test233", "1");
//        String value = stringRedisTemplate.opsForValue().get("test233");

        // 获得核心对象：jedis
        Jedis jedis = null;
        try{
            // 通过连接池来获得连接
            jedis = jedisPool.getResource();
//            // 设置数据
            jedis.set("name","张三2");
//            // 获取数据
            String value = jedis.get("name");
            System.out.println(value);
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            // 释放资源
            if (jedis != null) {
                jedis.close();
            }
            // 释放连接池
            if (jedisPool != null) {
                jedisPool.close();
            }
        }

    }

    public static void main(String[] args) {
        PaymentService paymentService = new PaymentService();
        paymentService.redisTest();
    }
}
