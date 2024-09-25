package com.green.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.MemberInfoDTO;
import com.green.dto.ResponseMessage;
import com.green.entity.Member;
import com.green.repository.MemberRepository;
import com.green.service.MemberService;
import com.green.service.MemberSignServiceImpe;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/recover")
@RequiredArgsConstructor
public class PasswordRecoverController {

	private final MemberService	 service;
	private final MemberRepository memberRepository;
	private final MemberSignServiceImpe signService;
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping("/verification-requests")
	public ResponseEntity<ResponseMessage> checkInfo(@RequestParam String id, @RequestParam String email){
		ResponseMessage message = new ResponseMessage("입력하신 정보로 인증코드가 전송되었습니다.", null, true);
		MemberInfoDTO memberResult = service.findInfoByEmail(email);
		if(memberResult != null) {
			if(memberResult.getId().equals(id)) {
				signService.sendCodeToEmail(email, "recover");
				return ResponseEntity.ok().body(message);
			}
		}
		return ResponseEntity.ok().body(message);
	}
	
	@PostMapping("/verifications")
	public ResponseEntity<ResponseMessage> verification(@RequestParam("email") String email, @RequestParam("authCode") String authCode, HttpSession session){
		Map<String, String> messages = new HashMap<>();
		boolean checkResult = signService.verificateCode(email, authCode, "recover");
		
		if(checkResult) {
			session.setAttribute("isAuthenticated", true);
		    session.setAttribute("userEmail", email);
			messages.put("result", "이메일 인증이 완료되었습니다.");
		}else {
			messages.put("result", "입력된 정보가 유효하지 않습니다.");
		}
		
		return checkResult ?
				ResponseEntity.ok().body(new ResponseMessage(messages, true, "/resetPassword"))
				: ResponseEntity.ok().body(new ResponseMessage(messages, false, null));
	}
	
	@PostMapping("/resetPassword")
	public ResponseEntity<ResponseMessage> changePassword(@RequestParam("password") String password, HttpSession session){
		boolean isAuthenticated = (boolean)session.getAttribute("isAuthenticated");
		String userEmail = (String)session.getAttribute("userEmail");
		
		Map<String, String> messages = new HashMap<>();
		if(isAuthenticated) {
			Member member = service.findByEmail(userEmail);
			if(member != null) {
				member.setPassword(passwordEncoder.encode(password));
				System.out.println(member.toString());
				messages.put("result", "비밀번호 재설정이 완료되었습니다.");
				memberRepository.save(member);
				session.invalidate();
				return ResponseEntity.ok().body(new ResponseMessage(messages, true, "/login"));
			}			
		}
		
		messages.put("result", "잘못된 접근입니다.");
		return ResponseEntity.ok().body(new ResponseMessage(messages, false, "/"));
	}
	
	
}
