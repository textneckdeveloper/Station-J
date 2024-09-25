package com.green.dto;

import java.time.LocalDateTime;
import java.util.Set;

import com.green.entity.MemberRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoDTO {
	
	private Long num;
	private String id;
	private String nickName;
	private String email;
	private LocalDateTime regDate;
	private Set<String> roleSet;
	
}
