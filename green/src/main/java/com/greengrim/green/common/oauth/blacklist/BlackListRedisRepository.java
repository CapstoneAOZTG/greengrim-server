package com.greengrim.green.common.oauth.blacklist;

import jakarta.annotation.Resource;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BlackListRedisRepository {

    private static final String LOGOUT = "LOGOUT";

    @Resource(name = "redisTemplate")
    private ValueOperations<String, String> valueOperations;

    public void save(String accessToken, Long duration) {
        valueOperations.set(accessToken, LOGOUT, duration, TimeUnit.MILLISECONDS);
    }

    public boolean existsById(String accessToken) {
        return (valueOperations.get(accessToken) != null);
    }

}
