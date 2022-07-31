package com.example.demo.controller;

import javax.validation.Valid;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.AuthService;
import com.example.demo.service.dto.request.LoginRequestDTO;
import com.example.demo.service.dto.response.LoginResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthenticationController {

    private final AuthService authService;
    
	@PostMapping("/user/login")
	public ResponseEntity<LoginResponseDTO> loginUser(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
		return ResponseEntity.ok(authService.login(loginRequestDTO, "USER"));
	}
	
	@PostMapping("/staff/login")
	public ResponseEntity<LoginResponseDTO> loginStaff(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
		return ResponseEntity.ok(authService.login(loginRequestDTO, "STAFF"));
	}
	
	@PostMapping("/admin/login")
	public ResponseEntity<LoginResponseDTO> loginAdmin(@Valid @RequestBody LoginRequestDTO loginRequestDTO) {
		return ResponseEntity.ok(authService.login(loginRequestDTO, "ADMIN"));
	}
	
	@PostMapping("/logout")
	public ResponseEntity<?> logout(@RequestHeader HttpHeaders headers) {
		String token = headers.getFirst(HttpHeaders.AUTHORIZATION);
		return ResponseEntity.ok(authService.logout(token.split(" ")[1]));
	}
}