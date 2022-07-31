package com.example.demo.service;

import com.example.demo.service.dto.request.LoginRequestDTO;
import com.example.demo.service.dto.response.LoginResponseDTO;

public interface AuthService {
	LoginResponseDTO login(LoginRequestDTO loginRequestDTO, String user_role);
	String logout(String jwt);
}
