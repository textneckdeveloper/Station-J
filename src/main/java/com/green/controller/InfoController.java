package com.green.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.MemberDTO;
import com.green.dto.MemberInfoDTO;
import com.green.dto.ResponseMessage;
import com.green.dto.SignUpSocialMemberCommand;
import com.green.entity.Member;
import com.green.security.dto.AuthMemberDetails;
import com.green.service.MemberService;
import com.green.service.MemberSignServiceImpe;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/info")
@RequiredArgsConstructor
public class InfoController {
	
	private final MemberService service;
	private final MemberSignServiceImpe signService;

	@GetMapping("/myinfo")
	public ResponseEntity<ResponseMessage> viewMyInfo(@AuthenticationPrincipal AuthMemberDetails authMember) {
		MemberInfoDTO memberInfo = MemberInfoDTO.builder()
													.id(authMember.getId())
													.nickName(authMember.getNickName())
													.email(authMember.getEmail())
													.roleSet(authMember.getAuthorities().stream().map(auth->auth.getAuthority()).collect(Collectors.toSet()))
													.build();
		return ResponseEntity.ok().body(new ResponseMessage(memberInfo, true));
	}
	
	// 소셜 가입 이용자 추가정보 입력창 or 모든유저 전용 정보 수정
	@GetMapping("/editinfo")
	public String goToEditInfo(Model model, @AuthenticationPrincipal AuthMemberDetails socialAuthMember ) {
		model.addAttribute("memberEmail", socialAuthMember.getEmail());
		return "/info/editinfo";
	}
	
	// 소셜 가입 이용자가 추가정보입력한 뒤 데이터베이스에 저장
	@PostMapping("/editinfo")
	public ResponseEntity<ResponseMessage> finishEditInfo(@RequestBody @Valid SignUpSocialMemberCommand command, Errors errors,
															 @AuthenticationPrincipal AuthMemberDetails authMember) {
		Map<String, String> validatorResult = new HashMap<>();
		Map<String, String> messages = new HashMap<>();
		// 유효성 검사
		if(errors.hasErrors()) {
			validatorResult = signService.validateHandling(errors);
			for(String key : validatorResult.keySet()) {
				messages.put(key, validatorResult.get(key));
			}
			return ResponseEntity.ok().body(new ResponseMessage(messages, false, null));
		}
		MemberDTO memberDTO = MemberDTO.builder()
										.num(authMember.getNum())
										.nickName(command.getNickName())
										.email(authMember.getEmail())
										.build();
		Member member = service.registerSocialMember(memberDTO);
		authMember.setNickName(command.getNickName());
		
		messages.put("result", "가입이 완료되었습니다.");
		
		return ResponseEntity.ok().body(new ResponseMessage(messages, true, "/"));
	}
}
