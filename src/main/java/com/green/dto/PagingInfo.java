package com.green.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingInfo {

	private int pageNum;
	private int amount;
	
	public PagingInfo() {
		this(1, 9);
	}
	
}
