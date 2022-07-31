package com.example.demo.service.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class LoginRequestDTO {
    @NotBlank
    @Email(message = "Wrong format email")
	private String email;
	
    @NotBlank(message = "The password is required")
	private String password;
}