package com.green.dto;

import lombok.Data;

@Data
public class PageDTO {

	private int startPage;
	private int endPage;
	
	private boolean prev;
	private boolean next;
	
	private long total;
	private PagingInfo pagingInfo;
	
	public PageDTO(PagingInfo pagingInfo, long total) {
		
		this.pagingInfo = pagingInfo;
		this.total = total;
		
		this.endPage = ((pagingInfo.getPageNum()+9)/10)*10;
		this.startPage = this.endPage-9;
		
		int realEndPage = (int)(Math.ceil(this.total/(pagingInfo.getAmount()*1.0)));
		
		if(realEndPage < this.endPage) {
			this.endPage = realEndPage;
		}
		
		this.prev = startPage != 1;
		this.next = this.endPage < realEndPage;
		
	}

}
