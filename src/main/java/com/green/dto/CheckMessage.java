package com.green.dto;

import lombok.Data;

@Data
public class CheckMessage {

	private boolean success;
	private String message;
	
	
	public CheckMessage(String message, boolean success) {
		this.message = message;
		this.success = success;
	}
	
}
