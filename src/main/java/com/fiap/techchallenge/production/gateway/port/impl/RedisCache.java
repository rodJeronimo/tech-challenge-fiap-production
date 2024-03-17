package com.fiap.techchallenge.production.gateway.port.impl;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fiap.techchallenge.production.gateway.port.CachePort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisCache implements CachePort {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public Object getValueByKey(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public void setKeyWithoutExpirationTime(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

}
