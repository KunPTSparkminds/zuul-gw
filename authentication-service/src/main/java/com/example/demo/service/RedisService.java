package com.example.demo.service;

public interface RedisService {
	void cacheJWT(String jwt, Long expiredTime);
}
