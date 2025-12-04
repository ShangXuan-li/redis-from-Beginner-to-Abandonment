package com.lsx;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lsx.redis.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Map;

@SpringBootTest
class RedisStringTests {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Test
    void testString() {
        // 写入一条String数据
        stringRedisTemplate.opsForValue().set("name", "虎哥");
        // 获取string数据
        Object name = stringRedisTemplate.opsForValue().get("name");
        System.out.println("name = " + name);
    }

    @Test
    void testSaveUser() throws JsonProcessingException {
        // 1.创建一个Json序列化对象
        ObjectMapper objectMapper = new ObjectMapper();
        // 2.将要存入的对象通过Json序列化对象转换为字符串
        String userJson = objectMapper.writeValueAsString(new User("虎哥", 21));
        // 3.通过StringRedisTemplate将数据存入redis
        stringRedisTemplate.opsForValue().set("user:100", userJson);
        // 4.通过key取出value
        String jsonUser = stringRedisTemplate.opsForValue().get("user:100");
        // 5.由于取出的值是String类型的Json字符串，因此我们需要通过Json序列化对象来转换为java对象
        User user = objectMapper.readValue(jsonUser, User.class);
        System.out.println("user = " + user);
    }

    @Test
    void testHash() {
        stringRedisTemplate.opsForHash().put("user:400", "name", "虎哥");
        stringRedisTemplate.opsForHash().put("user:400", "age", "21");

        Map<Object, Object> entries = stringRedisTemplate.opsForHash().entries("user:400");
        System.out.println("entries = " + entries);
    }

    @Test
    void contextLoads() {
    }

}
