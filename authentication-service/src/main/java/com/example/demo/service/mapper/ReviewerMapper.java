package com.example.demo.service.mapper;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Reviewer;
import com.example.demo.service.dto.response.ReviewerResponseDTO;

@Service
public class ReviewerMapper {
	public ReviewerResponseDTO entityToResponse(Reviewer entity) {
		if (entity == null)
			return null;

		ReviewerResponseDTO responseDto = new ReviewerResponseDTO();
		responseDto.setEmail(entity.getEmail());
		responseDto.setName(entity.getName());
		responseDto.setPicture(entity.getPicture());
		return responseDto;
	}
}
