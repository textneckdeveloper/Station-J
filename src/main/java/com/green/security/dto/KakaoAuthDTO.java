package com.green.security.dto;

import java.util.Map;

public class KakaoAuthDTO implements AuthMemberDTO{
	private Map<String,Object> attributes;
	private Map<String,Object> accountAttributes;
	
	public KakaoAuthDTO(Map<String, Object> attributes) {
		this.attributes = attributes;
		this.accountAttributes = (Map<String, Object>) attributes.get("kakao_account");
	}
	
	@Override
    public String getProviderId() {
        return attributes.get("id").toString();
    }
	
    @Override
    public String getProvider() {
        return "kakao";
    }

    @Override
    public String getEmail() {
        return accountAttributes.get("email").toString();
    }
	
	
}
