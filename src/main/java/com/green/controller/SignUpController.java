package com.green.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.MemberDTO;
import com.green.dto.ResponseMessage;
import com.green.dto.SignUpMemberCommand;
import com.green.security.validation.ValidationSequence;
import com.green.service.MemberService;
import com.green.service.MemberSignServiceImpe;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/signup")
@RequiredArgsConstructor
public class SignUpController {
	
	private final MemberService service;
	private final MemberSignServiceImpe signService;
	private final PasswordEncoder passwordEncoder;
	
	@PostMapping("/submit")
	public ResponseEntity<ResponseMessage> test(@Validated(ValidationSequence.class) @RequestBody SignUpMemberCommand command, Errors errors) {
		System.out.println(command.toString());
		Map<String, String> validatorResult = new HashMap<>();
		Map<String, String> messages = new HashMap<>();
		System.out.println(errors.toString());
		// 유효성 검사
		if(errors.hasErrors()) {
			validatorResult = signService.validateHandling(errors);
			for(String key : validatorResult.keySet()) {
				messages.put(key, validatorResult.get(key));
			}
			if(!command.isAgreement()) {
				messages.put("agreementErrorMsg", "약관에 동의해야 합니다.");
			}
			if(!command.isEmailVerificated()) {
				messages.put("emailVerificatedErrorMsg", "이메일 인증을 완료해야 합니다.");
			}
			return ResponseEntity.ok().body(new ResponseMessage(messages, false, null));
		}

		// 모두 통과시 회원가입 시도
		MemberDTO memberDTO = MemberDTO.builder()
				.nickName(command.getNickName())
				.id(command.getId())
				.password(passwordEncoder.encode(command.getPassword()))
				//.password("{plaintext}" + command.getPassword()) // 평문저장 인코더 사용시
				.email(command.getEmail())
				.fromSocial(false)
				.build();
		
		messages.put("result", service.registerMember(memberDTO));
		
		return ResponseEntity.ok().body(new ResponseMessage(messages, true, "/login"));
	}
	
	@GetMapping("")
	public String goToSignUp() {
		return "/main/signuppage";
	}
	
	@GetMapping("/idcheck/{id}")
	public ResponseEntity<ResponseMessage> checkIdAlreadyExist(@PathVariable("id") String id) {
		boolean checkResult = service.checkAlreadyExistMemberById(id);
		
		return checkResult
			? ResponseEntity.ok().body(new ResponseMessage("사용할 수 없는 아이디 입니다. 다른 아이디를 입력해주세요.", id, false))
			: ResponseEntity.ok().body(new ResponseMessage("사용 가능한 아이디 입니다.", id, true));
	}
	
	@GetMapping("/emailcheck/{email}")
	public ResponseEntity<ResponseMessage> checkEmailAlreadyExist(@PathVariable("email") String email) {
		boolean checkResult = service.checkAlreadyExistMemberByEmail(email);

		return checkResult
			? ResponseEntity.ok().body(new ResponseMessage("사용할 수 없는 이메일 입니다. 다른 이메일을 입력해주세요.", email, false))
			: ResponseEntity.ok().body(new ResponseMessage("사용 가능한 이메일 입니다.", email, true));
	}
		
	@PostMapping("/email/verification-requests")
	public ResponseEntity<ResponseMessage> sendMessage(@RequestParam("checkedEmail") String email) {
		signService.sendCodeToEmail(email, "signUp");
		return ResponseEntity.ok().body(new ResponseMessage("전송완료", email, true));
	}
		
	@PostMapping("/email/verifications")
	public ResponseEntity<ResponseMessage> verificationEmail(@RequestParam("checkedEmail") String email, @RequestParam("authCode") String authCode) {
		//	이메일, 인증코드 검증
		boolean checkResult = signService.verificateCode(email, authCode, "signUp");
	
		return checkResult
				? ResponseEntity.ok().body(new ResponseMessage("이메일 인증이 완료되었습니다.", email, true))
				: ResponseEntity.ok().body(new ResponseMessage("만료되었거나 올바른 인증번호가 아닙니다.", email, false));
	}
}
