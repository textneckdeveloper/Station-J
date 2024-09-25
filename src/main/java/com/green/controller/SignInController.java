package com.green.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.SignInMemberCommand;

import jakarta.servlet.http.Cookie;


@RestController
@RequestMapping("/signin")
public class SignInController {
	
	@GetMapping("")
	public String goToSignIn(Model model,
							@CookieValue(value = "rememberedUsername", required = false) Cookie cookie) {
		SignInMemberCommand signInMember = new SignInMemberCommand();
		if(cookie != null) {
			signInMember.setUsername(cookie.getValue());
			signInMember.setRememberUsername(true);
			model.addAttribute("signInMemberCommand", signInMember);
		}
		return "/main/signinpage";
	}
	
	@GetMapping("/error")
	public ResponseEntity<String> signInError(){
		return ResponseEntity.ok().body("아이디 혹은 비밀번호가 일치하지 않습니다.");
	}
}
