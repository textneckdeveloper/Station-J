package com.green.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.green.dto.MemberDTO;
import com.green.entity.Member;
import com.green.repository.MemberRepository;
/*
@SpringBootTest
public class InsertAdmin implements MemberService {

	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void insertAdmin() {
		MemberDTO memberDTO = MemberDTO.builder()
								.email("admin@admin.com")
								.id("admin")
								.password(passwordEncoder.encode("admin"))
								.fromSocial(false)
								.nameFirst("어드민")
								.nameSecond("")
								.build();
		
		Member member = dtoToEntity(memberDTO);
		member.addMemberRole(MemberRole.USER);
		member.addMemberRole(MemberRole.STAFF);
		member.addMemberRole(MemberRole.ADMIN);
		
		memberRepository.save(member);
	}

	@Override
	public String registerMember(MemberDTO memberDTO) {
		return null;
	}

	@Test
	public void deleteAdmin() {
		
	}

	@Override
	public String registerSocialMember(MemberDTO memberDTO) {
		return null;
	}
}
*/
