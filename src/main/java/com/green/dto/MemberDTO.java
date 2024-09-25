package com.green.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDTO {
	
	private Long num;
	private String id;
	private String password;
	private String email;
	private String nickName;
	private boolean fromSocial;
	private LocalDateTime regDate, modDate;
	
}
