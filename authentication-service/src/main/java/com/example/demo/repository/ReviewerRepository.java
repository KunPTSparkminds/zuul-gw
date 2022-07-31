package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.Reviewer;

public interface ReviewerRepository extends JpaRepository<Reviewer, Long>{
	boolean existsByEmail(String email);
}
