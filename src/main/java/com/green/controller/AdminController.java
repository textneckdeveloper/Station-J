package com.green.controller;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.MemberInfoDTO;
import com.green.dto.MemberListDTO;
import com.green.dto.ResponseMessage;
import com.green.dto.RoleDTO;
import com.green.dto.SiteInfoDTO;
import com.green.entity.Member;
import com.green.repository.MemberRepository;
import com.green.repository.RoleRepository;
import com.green.service.MainService;
import com.green.service.MemberService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
	
	private final MemberService service;
	private final MemberRepository repository;
	private final RoleRepository roleRepository;
	private final MainService mainService;

	@GetMapping("/getSiteInfo")
	public ResponseEntity<SiteInfoDTO> getSiteInfo(){
		SiteInfoDTO siteInfo = mainService.getSiteInfo();
		return ResponseEntity.ok().body(siteInfo);
	}
	
	// 계정 목록
	@GetMapping("/getMemberList")
	public ResponseEntity<MemberListDTO> getMemberList(@RequestParam("pageNum") int pageNum){
		Sort sortByRegDate = Sort.by("regDate").descending();
		Pageable pageable = PageRequest.of(pageNum-1, 10, sortByRegDate);
		Page<Member> result = repository.findAll(pageable);
		
		MemberListDTO memberList = new MemberListDTO(result);
		result.get().forEach(member->{
			MemberInfoDTO memberInfo = service.entityToDtoForGetMember(member);
			memberList.getMembers().add(memberInfo);
		});
		return ResponseEntity.ok().body(memberList);
	}
	
	// 계정 검색
	@GetMapping("/searchMemberList")
	public ResponseEntity<MemberListDTO> getSearchMember(@RequestParam("searchQuery") String seachQuery, @RequestParam("pageNum") int pageNum){
		
		Sort sortByRegDate = Sort.by("regDate").descending();
		Pageable pageable = PageRequest.of(pageNum-1, 10, sortByRegDate);
		Page<Member> result = repository.getMemberSearchResult(seachQuery, pageable);
		
		MemberListDTO memberList = new MemberListDTO(result);
		result.get().forEach(member-> {
			MemberInfoDTO memberInfo = service.entityToDtoForGetMember(member);
			memberList.getMembers().add(memberInfo);
		});
		
		return ResponseEntity.ok().body(memberList);
	}
	
	// 계정 상세보기
	@GetMapping("/memberDetail/{num}")
	public ResponseEntity<ResponseMessage> getMemberDetail(@PathVariable("num") Long num){
		Optional<Member> result = repository.findByNum(num);
		MemberInfoDTO memberInfo = null;
		if(result.isPresent()) {
			Member member = result.get();
			memberInfo = MemberInfoDTO.builder().num(member.getNum())
															.nickName(member.getNickName())
															.email(member.getEmail())
															.id(member.getId())
															.regDate(member.getRegDate())
															.roleSet(member.getRoleSet().stream().map(set->set.getRole().getRoleName()).collect(Collectors.toSet()))
															.build();
			return ResponseEntity.ok().body(new ResponseMessage(memberInfo, true));
		}else {
			return ResponseEntity.ok().body(new ResponseMessage(null, false));
		}
		
	}
	
	// 권한 수정
	@PutMapping("/modifyMember/{num}")
	public ResponseEntity<ResponseMessage> changeMemberDetail(@PathVariable("num") Long num, @RequestBody RoleDTO roleDTO) {		
		Optional<Member> result = repository.findById(num);
		if(result.isPresent()) {
			Member member = result.get();
			member.removeRole(roleRepository.findByRoleName("STAFF").orElse(null));
			member.removeRole(roleRepository.findByRoleName("SUBADMIN").orElse(null));
			member.addRole(roleRepository.findByRoleName(roleDTO.getSelectedRole()).orElse(null));
			repository.save(member);
			return ResponseEntity.ok().body(new ResponseMessage("변경되었습니다.", null, true));
		}
		return ResponseEntity.ok().body(new ResponseMessage("잘못된 접근입니다.", null, false));
	}
	
	// 계정 삭제
	@DeleteMapping("/deleteMember/{num}")
	public ResponseEntity<ResponseMessage> deleteMember(@PathVariable("num") Long num) {
		Optional<Member> result = repository.findByNum(num);
		if(result.isPresent()) {
			repository.deleteById(num);
			return ResponseEntity.ok().body(new ResponseMessage("삭제되었습니다.", null, true));
		}
		return ResponseEntity.ok().body(new ResponseMessage("없는 회원입니다.", null, false));
	}
	
	
}
