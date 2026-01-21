package com.example.taskmanager.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;


@SpringBootTest
public class RedisTest {
    @Autowired
    private  RedisTemplate redisTemplate;

    @Test
    void testRedis(){
       // redisTemplate.opsForValue().set("email","abc@gmail.com");
        //redisTemplate.opsForValue().set("age",21);
        System.out.println(redisTemplate.opsForValue().get("age"));
    }
}
