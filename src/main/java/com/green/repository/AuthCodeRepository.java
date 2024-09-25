package com.green.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.green.entity.EmailAuthData;

public interface AuthCodeRepository extends JpaRepository<EmailAuthData, Long>{
	
	@Query("SELECT a FROM EmailAuthData a WHERE email = :email")
	Optional<EmailAuthData> findByEmail(String email);

}
