package com.green.security.dto;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthMemberDetails extends User implements OAuth2User{
	
	private Long num;
	private String nickName;
	private String id;
	private String email;
	private String password;
	private boolean fromSocial;
	private Map<String, Object> attributes;
	
	// 일반 로그인시 생성자
	public AuthMemberDetails(Long num, String username, String password, String nickName, String email, boolean fromSocial,
							Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.id = username;
		this.password = password;
		this.num = num;
		this.email = email;
		this.fromSocial = fromSocial;
		this.nickName = nickName;
	}
	
	
	// 소셜 로그인시 생성자
	public AuthMemberDetails(Long num, String username, String password, String nickName, String email, boolean fromSocial,
							Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes) {
		super(username, password, authorities);
		this.num = num;
		this.id = email;
		this.email = email;
		this.fromSocial = fromSocial;
		this.nickName = nickName;
		this.attributes = attributes;
	}


	@Override
	public Map<String, Object> getAttributes() {
		return this.attributes;
	}

	@Override
	public String getName() {
		return this.nickName;
	}

}
