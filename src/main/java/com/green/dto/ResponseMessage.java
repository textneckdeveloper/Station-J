package com.green.dto;

import java.util.Map;

import lombok.Data;

@Data
public class ResponseMessage {
	
	private boolean success;
	private Map<String, String> messages;
	private String message;
	private String payload;
	private String redirectUrl;
	private MemberInfoDTO memberInfo;
	
	public ResponseMessage(Map<String, String> messages, boolean success, String redirectUrl) {
		this.messages = messages;
		this.success = success;
		this.redirectUrl = redirectUrl;
	}
	
	public ResponseMessage(String message, String payload, boolean success) {
		this.message = message;
		this.payload = payload;
		this.success = success;
	}
	
	public ResponseMessage(MemberInfoDTO memberInfo, boolean success) {
		this.memberInfo = memberInfo;
		this.success = success;
	}
	
}
