package com.green.service;

import java.util.Map;
import java.util.Optional;

import org.springframework.validation.Errors;

import com.green.entity.Member;


public interface MemberSignService {
	
	public Map<String, String> validateHandling(Errors errors);
	
	
}
