package com.green.service;

import java.util.List;
import java.util.stream.Collectors;

import com.green.dto.MemberDTO;
import com.green.dto.MemberInfoDTO;
import com.green.entity.Member;

public interface MemberService {
	
	// 회원가입 관련 메서드
	public String registerMember(MemberDTO memberDTO);
	public Member registerSocialMember(MemberDTO memberDTO);
	public MemberInfoDTO findInfoByEmail(String userEmail);
	public boolean checkAlreadyExistMemberById(String username);
	public boolean checkAlreadyExistMemberByEmail(String email);
	
	// 회원조회 및 권한 관련 메서드
	public List<MemberInfoDTO> getAllMemberList();
	public Member findByEmail(String userEmail);
	
	// 회원조회용 엔티티-DTO 전환 메서드
	default MemberInfoDTO entityToDtoForGetMember(Member member) {
		MemberInfoDTO memberInfo = MemberInfoDTO.builder()
												.num(member.getNum())												
												.id(member.getId())
												.nickName(member.getNickName())
												.email(member.getEmail())
												.regDate(member.getRegDate())
												.roleSet(member.getRoleSet().stream()
														.map(role->role.getRole().getRoleName())
														.collect(Collectors.toSet()))
												.build();
		return memberInfo;
	}
	
	
	// 엔티티-DTO간 전환 메서드
	default Member dtoToEntity(MemberDTO memberDTO) {
		Member member = Member.builder()
								.nickName(memberDTO.getNickName())
								.id(memberDTO.getId())
								.password(memberDTO.getPassword())
								.email(memberDTO.getEmail())
								.fromSocial(memberDTO.isFromSocial())
								.build();
		// 권한 넘기기
		
		return member;
	}
	
	default MemberDTO entityToDTO(Member member) {
		MemberDTO memberDTO = MemberDTO.builder()
										.nickName(member.getNickName())
										.id(member.getId())
										.password(member.getPassword())
										.email(member.getEmail())
										.fromSocial(member.isFromSocial())
										.build();
		// 권한 넘기기
		
		return memberDTO;
	}
	
}
