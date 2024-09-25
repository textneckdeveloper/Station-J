package com.green.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.MemberInfoDTO;
import com.green.dto.PageDTO;
import com.green.dto.PagingInfo;
import com.green.dto.ResponseMessage;
import com.green.security.dto.AuthMemberDetails;
import com.green.service.MainService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value={"/", "/main"})
@RequiredArgsConstructor
public class MainController {

	private final MainService service;
	
	@GetMapping("/api/csrf-token")
	public ResponseEntity<Map<String, String>> getCsrfToken(HttpServletRequest request) {
	    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
	    Map<String, String> tokens = new HashMap<>();
	    tokens.put("token", csrf.getToken());
	    return ResponseEntity.ok(tokens);
	}
	
	
	@GetMapping("/userAuthentication")
	public ResponseEntity<ResponseMessage> getUserAuth(@AuthenticationPrincipal AuthMemberDetails authMember){
		if(authMember != null) {
			MemberInfoDTO memberInfo = MemberInfoDTO.builder()
					.id(authMember.getId())
					.nickName(authMember.getNickName())
					.email(authMember.getEmail())
					.roleSet(authMember.getAuthorities().stream().map(auth->auth.getAuthority()).collect(Collectors.toSet()))
					.build();
			
			if(authMember.getNickName() == null) {
				return ResponseEntity.ok().body(new ResponseMessage("추가정보 입력이 필요합니다.", null, false));
			}
			return ResponseEntity.ok().body(new ResponseMessage(memberInfo, true));
		}
		
		return ResponseEntity.ok().body(null);
	}
	
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> search(@RequestParam("searchQuery") String searchQuery, PagingInfo pagingInfo){
		long total = service.getSearchBoardCount(searchQuery);
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("board", service.boardSearch(pagingInfo, searchQuery));
		response.put("page", new PageDTO(pagingInfo, total));
		
		return ResponseEntity.ok(response);
	}
	
}
