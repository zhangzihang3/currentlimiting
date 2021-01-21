package com.zzh.currentlimiting;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
class CurrentlimitingApplicationTests {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void contextLoads() {
        System.out.println(stringRedisTemplate.opsForValue().get("login"));
    }

}
