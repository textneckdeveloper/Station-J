package com.green.security.service;

import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.green.entity.Member;
import com.green.repository.MemberRepository;
import com.green.security.dto.AuthMemberDetails;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberUserDetailsService implements UserDetailsService{
	// 로그인 시 입력받은 데이터로 인증확인 후 UserDetails에 권한 부여(인가)해주기
	
	
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Member> result = memberRepository.findById(username, false);

		if(!result.isPresent()) {
			// DB에 입력된 username 이 없는 경우
			System.out.println("입력된 유저이름 : " + username);
			System.out.println("유저 없음");
			return null;
		}
		Member member = result.get();
		AuthMemberDetails memberAuth = new AuthMemberDetails(
													member.getNum(),
													member.getId(),
													member.getPassword(),
													member.getNickName(),
													member.getEmail(),
													false,
													member.getRoleSet().stream().map((role)->
														new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName()))
														.collect(Collectors.toSet())
													);
		return memberAuth;
	}

}
