package com.example.demo.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.example.demo.service.RedisService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {
	private final RedisTemplate<String, Object> redisTemplate;
	@Override
	public void cacheJWT(String jwt, Long expiredTime) {
		redisTemplate.opsForValue().set(jwt, jwt, expiredTime, TimeUnit.HOURS);

	}

}
