package com.green.service;

import java.util.Optional;
import java.util.Set;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.annotation.Rollback;

import com.green.entity.Member;
import com.green.entity.MemberRole;
import com.green.entity.Role;
import com.green.repository.MemberRepository;
import com.green.repository.MemberRoleRepository;
import com.green.repository.RoleRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class InsertSamples {
	
	@Autowired
	private MemberRepository memberRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private MemberRoleRepository mrRepository;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Test
	public void insertRoles() {
		Role role1 = Role.builder().roleName("USER").build();
		Role role2 = Role.builder().roleName("STAFF").build();
		Role role3 = Role.builder().roleName("SUBADMIN").build();
		Role role4 = Role.builder().roleName("ADMIN").build();
		roleRepository.save(role1);
		roleRepository.save(role2);
		roleRepository.save(role3);
		roleRepository.save(role4);
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void giveRoles() {
		Optional<Member> result = memberRepository.findById("park", false);
		
		if(result.isPresent()) {
			Member member = result.get();
			Role role = roleRepository.findByRoleName("ADMIN").orElse(null);
			member.addRole(role);
		}
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void removeRoles() {
		Optional<Member> result = memberRepository.findById("park", false);
		//Member result = memberRepository.findById("park", false).orElse(null);
		System.out.println(result.toString());
		if(result.isPresent()) {
			Member member = result.get();
			Role role = roleRepository.findByRoleName("ADMIN").orElse(null);
			member.removeRole(role);
		}
	}
	
	@Test
	@Transactional
	public void checkRole() {
		Optional<Member> result = memberRepository.findById("park", false);
		System.out.println(result.isPresent());
		
		if (result.isPresent()) {
			Member member = result.get();
			System.out.println(member);
		}
	}
	
	
	
	@Test
	@Transactional
	@Rollback(false)
	public void insertOneMember() {
		Member member = Member.builder()
								.id("park")
								.password(passwordEncoder.encode("test"))
								.email("park@naver.com")
								.nickName("박")
								.build();
		Role role = roleRepository.findByRoleName("USER").orElse(null);
		member.addRole(role);
		memberRepository.save(member);
	}
	
	
	@Test
	@Transactional
	@Rollback(false)
	public void insertSampleMembers() {
		IntStream.range(1,50).forEach(i->{
			Member member = Member.builder()
									.id("testId" + i)
									.password(passwordEncoder.encode("1234"))
									.email("testEmail_" + i + "@naver.com")
									.nickName("testNickName_" + i)
									.build();
			member.addRole(roleRepository.findByRoleName("USER").orElse(null));
			memberRepository.save(member);
		});
	}
	
	@Test
	@Transactional
	@Rollback(false)
	public void insertAdmin() {
		Member member = Member.builder()
								.id("admin")
								.password(passwordEncoder.encode("1234"))
								.email("admin@admin.com")
								.nickName("admin")
								.build();
		member.addRole(roleRepository.findByRoleName("USER").orElse(null));
		member.addRole(roleRepository.findByRoleName("ADMIN").orElse(null));
		memberRepository.save(member);
	}
}
