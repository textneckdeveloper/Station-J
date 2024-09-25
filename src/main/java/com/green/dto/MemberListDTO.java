package com.green.dto;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

import com.green.entity.Member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MemberListDTO {
	private List<MemberInfoDTO> members = new ArrayList<>();
	private int num;
	private int size;
	private Long totalElements;
	private int totalPages;
	private boolean isFirst;
	private boolean isLast;
	
	public MemberListDTO(Page<Member> page) {
		this.num = page.getNumber() + 1;
		this.size = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
		this.isFirst = page.isFirst();
		this.isLast = page.isLast();
	}
}
