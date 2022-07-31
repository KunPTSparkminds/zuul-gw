package com.example.demo.service.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Reviewer;
import com.example.demo.repository.ReviewerRepository;
import com.example.demo.security.JwtTokenProvider;
import com.example.demo.service.AuthService;
import com.example.demo.service.RedisService;
import com.example.demo.service.SendingMessageService;
import com.example.demo.service.dto.request.LoginRequestDTO;
import com.example.demo.service.dto.request.RegisterRequestDTO;
import com.example.demo.service.dto.response.LoginResponseDTO;
import com.example.demo.service.dto.response.ReviewerResponseDTO;
import com.example.demo.service.mapper.ReviewerMapper;

import lombok.RequiredArgsConstructor;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
	private final ReviewerRepository reviewerRepository;
	private final JwtTokenProvider jwtTokenProvider;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final ReviewerMapper reviewerMapper;
	private final RedisService redisService;
	private final SendingMessageService sendingMessageService;
	@Override
	public LoginResponseDTO login(LoginRequestDTO loginRequestDTO, String user_role) {
		sendingMessageService.sendingMessage(loginRequestDTO.getEmail(), "log in");
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
		sendingMessageService.sendingMessage("User", "log out");
		return "Logout Success";
	}

	@Override
	@Transactional
	public ReviewerResponseDTO register(RegisterRequestDTO registerRequestDTO) {
		if (reviewerRepository.existsByEmail(registerRequestDTO.getEmail()))
			return null;
		Reviewer reviewer = new Reviewer();
		reviewer.setEmail(registerRequestDTO.getEmail());
		reviewer.setName(registerRequestDTO.getName());
		reviewer.setPicture(registerRequestDTO.getPicture());
		reviewer.setRole(registerRequestDTO.getRole());
		reviewer.setPassword(bCryptPasswordEncoder.encode(registerRequestDTO.getPassword()));
		reviewerRepository.save(reviewer);
		sendingMessageService.sendingMessage(registerRequestDTO.getEmail(), "log in");
		return reviewerMapper.entityToResponse(reviewer);
	}

}
