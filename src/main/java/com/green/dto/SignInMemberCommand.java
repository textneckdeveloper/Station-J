package com.green.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInMemberCommand {

	private String username;
	
	private String password;
	
	private boolean rememberUsername;
	
}
