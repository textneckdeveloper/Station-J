package com.green.security.dto;

import java.util.Map;

public class GoogleAuthDTO implements AuthMemberDTO{

	private Map<String,Object> attributes;
	
	public GoogleAuthDTO(Map<String,Object> attributes) {
		this.attributes = attributes;
	}

	@Override
	public String getProviderId() {
		return this.attributes.get("sub").toString();
	}
	
	@Override
	public String getProvider() {
		return "google";
	}

	@Override
	public String getEmail() {
		return this.attributes.get("email").toString();
	}

}
