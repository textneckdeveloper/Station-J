package com.green.service;

import java.util.List;

import com.green.dto.BoardDTO;
import com.green.dto.PagingInfo;
import com.green.dto.SiteInfoDTO;

public interface MainService {
	
	public List<BoardDTO> boardSearch(PagingInfo pagingInfo, String searchQuery);
	public int getSearchBoardCount(String searchQuery);
	
	public SiteInfoDTO getSiteInfo();
	
}
