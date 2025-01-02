package com.ngbs.nanshu.gateway.logic.utils;

import cn.hutool.core.lang.generator.SnowflakeGenerator;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.ngbs.nanshu.gateway.logic.constant.RedisKey.WORK_ID;

@Component
public class IdGenerator implements ApplicationListener<ContextRefreshedEvent> {
    @Resource
    private RedisTemplate redisTemplate;

    private static SnowflakeGenerator SNOWFLAKE = null;

    public String generateId() {
        return SNOWFLAKE.next().toString();
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        Long workId = redisTemplate.opsForValue().increment(WORK_ID);
        if (workId == null) {
            Boolean b = redisTemplate.opsForValue().setIfAbsent(WORK_ID, 0);
            if (b) {
                workId = 0L;
            } else {
                workId = redisTemplate.opsForValue().increment(WORK_ID);
            }
        }
        if (workId == null) {
            throw new RuntimeException("workId must not empty");
        }
        SNOWFLAKE = new SnowflakeGenerator(workId, workId);
    }
}
