package com.green.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.green.dto.MemberDTO;
import com.green.dto.MemberInfoDTO;
import com.green.entity.Member;
import com.green.repository.MemberRepository;
import com.green.repository.RoleRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpe implements MemberService{

	
	private final MemberRepository memberRepository;
	private final RoleRepository roleRepository;
	
	@Override
	public String registerMember(MemberDTO memberDTO) {
		Member member = dtoToEntity(memberDTO);

		Optional<Member> selectedMember = memberRepository.findById(member.getId(), false);
		
		if(selectedMember.isPresent()) {
			return "이미 가입된 아이디입니다.";
		}
		member.addRole(roleRepository.findByRoleName("USER").orElse(null));
		memberRepository.save(member);
		return "회원가입이 완료되었습니다.";
	}
	
	@Transactional
	@Override
	public Member registerSocialMember(MemberDTO memberDTO) {
		Optional<Member> member = memberRepository.findById(memberDTO.getNum());
		if(!member.isPresent()) {
			return null;
		}
		Member newMember = member.get();
		newMember.setNickName(memberDTO.getNickName());
		
		return memberRepository.save(newMember);
	}

	@Override
	public MemberInfoDTO findInfoByEmail(String userEmail) {
		Optional<Member> members = memberRepository.findInfoByEmail(userEmail);
		if(!members.isPresent()) {
			return null;
		}
		Member member = members.get();
		MemberInfoDTO memberInfo = MemberInfoDTO.builder()
															.id(member.getId())
															.nickName(member.getNickName())
															.email(member.getEmail())
															.roleSet(member.getRoleSet().stream()
																	.map(role->role.getRole().getRoleName())
																	.collect(Collectors.toSet()))
															.build();
		return memberInfo; 
	}

	@Override
	public Member findByEmail(String userEmail) {
		Optional<Member> result = memberRepository.findByEmail(userEmail, false);
		if(result.isPresent()) {
			return result.get();
		}else {
			return null;
		}
	}
	
	@Override
	public boolean checkAlreadyExistMemberById(String username) {
		Optional<Member> result = memberRepository.findById(username, false);
		if(result.isPresent()) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public boolean checkAlreadyExistMemberByEmail(String email) {
		List<Member> result = memberRepository.findExistMemberByEmail(email);
		if(!result.isEmpty()) {
			return true;
		}else {
			return false;
		}
	}

	@Override
	public List<MemberInfoDTO> getAllMemberList() {
		List<Member> memberList = memberRepository.findAllMember();
		// 모든 멤버 조회 -> DTO변환 -> List<MemberDTO>형태로 반환
		return memberList.stream().map(member->entityToDtoForGetMember(member)).collect(Collectors.toList());
	}



	
}
