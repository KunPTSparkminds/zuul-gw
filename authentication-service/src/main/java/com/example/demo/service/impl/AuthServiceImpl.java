package com.example.demo.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import com.example.demo.service.RedisService;
import com.example.demo.service.dto.request.LoginRequestDTO;
import com.example.demo.service.dto.response.LoginResponseDTO;

import lombok.RequiredArgsConstructor;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	private final JwtTokenProvider jwtTokenProvider;
	private final RedisService redisService;
	@Override
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO, String user_role) {
		if (user_role == "STAFF") {
			return LoginResponseDTO
					.builder()
					.jwtToken(jwtTokenProvider.createJwtTokenStaff(100000000L))
					.build();
		} else if (user_role == "ADMIN") {
			return LoginResponseDTO
					.builder()
					.jwtToken(jwtTokenProvider.createJwtTokenAdmin(100000000L))
					.build();
		} else {
			return LoginResponseDTO
					.builder()
					.jwtToken(jwtTokenProvider.createJwtToken(100000000L))
					.build();
		}
	}

	@Override
	@Transactional
	public String logout(String jwt) {
		redisService.cacheJWT(jwt, (long) 8);
		return "Logout Success";
	}

}
