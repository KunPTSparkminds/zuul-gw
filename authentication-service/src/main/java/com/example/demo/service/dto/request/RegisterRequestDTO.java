package com.example.demo.service.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class RegisterRequestDTO {
    @NotBlank(message = "The name is required")
    private String name;
    
    @NotBlank(message = "The email is required")
    @Email(message = "Wrong email format")
    private String email;
    
    @NotBlank(message = "The password is required")
    private String password;
    
    @NotBlank(message = "The role is required")
    private String role;
    
    @NotBlank(message = "The picture is required")
    private String picture;
}