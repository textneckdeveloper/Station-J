package com.green.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.green.dto.BoardDTO;
import com.green.dto.PagingInfo;
import com.green.dto.SiteInfoDTO;
import com.green.entity.Board;
import com.green.repository.BoardRepository;
import com.green.repository.MemberRepository;
import com.green.repository.SectionRepository;

@Service
public class MainServiceImpe implements MainService {

	@Autowired
	BoardRepository br;
	
	@Autowired
	SectionRepository sr;
	
	@Autowired
	MemberRepository mr;
	
	@Override
	public List<BoardDTO> boardSearch(PagingInfo pagingInfo, String searchQuery) {
		
		Sort sort = Sort.by("regDate").descending();
		
		Pageable pageable = PageRequest.of(pagingInfo.getPageNum()-1, 9, sort);
		
		Page<Board> result = br.getSearchBoardResult(searchQuery, pageable);
		
		List<BoardDTO> list = new ArrayList<>();
		
		for(Board board : result) {
			
			BoardDTO boardDTO = new BoardDTO();
			
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setBoardTitle(board.getBoardTitle());
			boardDTO.setBoardContent(board.getBoardContent());
			boardDTO.setBoardWriteYear(board.getBoardWriteYear());
			boardDTO.setRegDate(board.getRegDate());
			boardDTO.setModDate(board.getModDate());
			boardDTO.setViewCount(board.getViewCount());
			boardDTO.setBoardFile(board.getBoardFile());
			boardDTO.setSectionNo(board.getSection().getSectionNo());
			
			list.add(boardDTO);
			
		}
		
		return list;
		
	}
	
	@Override
	public int getSearchBoardCount(String searchQuery) {
		
		Pageable pageable = PageRequest.of(0, 9);
		
		Page<Board> result = br.getSearchBoardResult(searchQuery, pageable);
		
		return (int)result.getTotalElements();
		
	}

	@Override
	public SiteInfoDTO getSiteInfo() {
		int totalContents = 0;
		int totalArchiveContents = 0;
		int totalVideoContents = 0;
		int totalMembers = 0;
		int totalMembersNotFromSocial = 0;
		int totalMembersFromSocial = 0;
		
		if(br.getBoardCount(2L).isPresent()) {
			totalArchiveContents = (Integer)br.getBoardCount(2L).get();
		}
		if(br.getBoardCount(3L).isPresent()) {
			totalVideoContents = (Integer)br.getBoardCount(3L).get();
		}
		totalContents = totalArchiveContents + totalVideoContents;
		
		if(mr.getMemberCountBySocial(false).isPresent()) {
			totalMembersNotFromSocial = (Integer)mr.getMemberCountBySocial(false).get();
		}
		if(mr.getMemberCountBySocial(true).isPresent()) {
			totalMembersFromSocial = (Integer)mr.getMemberCountBySocial(true).get();
		}
		totalMembers = totalMembersNotFromSocial + totalMembersFromSocial;
		
		SiteInfoDTO siteInfo = SiteInfoDTO.builder()
												.totalContents(totalContents)
												.totalArchiveContents(totalArchiveContents)
												.totalVideoContents(totalVideoContents)
												.totalMembers(totalMembers)
												.totalMembersFromSocial(totalMembersFromSocial)
												.build();
		return siteInfo;
	}
	
}
