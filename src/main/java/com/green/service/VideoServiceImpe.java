package com.green.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.green.dto.BoardDTO;
import com.green.dto.PagingInfo;
import com.green.entity.Board;
import com.green.repository.BoardRepository;
import com.green.repository.SectionRepository;

@Service
public class VideoServiceImpe implements VideoService{
	
	@Autowired
	BoardRepository br;
	
	@Autowired
	SectionRepository sr;
	
	@Override
	public List<BoardDTO> getMainVideoList() {
		
		Sort sort = Sort.by("regDate").descending();
		
		Pageable pageable = PageRequest.of(0, 3, sort);
		
		Page<Board> result = br.getBoardListPageAll(3L, pageable);
		
		List<BoardDTO> list = new ArrayList<>();
		
		for(Board board : result.getContent()) {
			
			BoardDTO boardDTO = new BoardDTO();
			
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setBoardTitle(board.getBoardTitle());
			boardDTO.setBoardContent(board.getBoardContent());
			boardDTO.setRegDate(board.getRegDate());
			boardDTO.setModDate(board.getModDate());
			boardDTO.setBoardFile(board.getBoardFile());
			boardDTO.setViewCount(board.getViewCount());
			boardDTO.setSectionNo(board.getSection().getSectionNo());
			
			list.add(boardDTO);
			
		}
		
		return list;
		
	}
	
	@Override
	public List<BoardDTO> getVideoList(PagingInfo pagingInfo) {
		
		Sort sort = Sort.by("regDate").descending();
		
		Pageable pageable = PageRequest.of(pagingInfo.getPageNum()-1, pagingInfo.getAmount()-1, sort);
		
		pagingInfo.setAmount(pagingInfo.getAmount()-1);

		Page<Board> result = br.getBoardListPageAll(3L, pageable);
		
		List<BoardDTO> list = new ArrayList<>();
		
		for(Board board : result.getContent()) {
			
			BoardDTO boardDTO = new BoardDTO();
			
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setBoardTitle(board.getBoardTitle());
			boardDTO.setBoardContent(board.getBoardContent());
			boardDTO.setRegDate(board.getRegDate());
			boardDTO.setModDate(board.getModDate());
			boardDTO.setBoardWriteYear(board.getBoardWriteYear());
			boardDTO.setViewCount(board.getViewCount());
			boardDTO.setBoardFile(board.getBoardFile());
			boardDTO.setSectionNo(board.getSection().getSectionNo());
			
			list.add(boardDTO);
			
		}
		
		return list;
		
	}
	
	@Override
	public int getVideoCount() {
		
		Pageable pageable = PageRequest.of(0, 8);
		
		Page<Board> result = br.getBoardListPageAll(3L, pageable);
		
		return (int)result.getTotalElements();
		
	}
	
	@Override
	public BoardDTO getVideoDetail(long boardNo, BoardDTO boardDTO) {
		
		Optional<Board> result = br.findByBoardNoAndSection_sectionNo(boardNo, 3L);
		
		if(result.isPresent()) {
			
			Board board = result.get();
			
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setBoardTitle(board.getBoardTitle());
			boardDTO.setBoardContent(board.getBoardContent());
			boardDTO.setRegDate(board.getRegDate());
			boardDTO.setModDate(board.getModDate());
			boardDTO.setBoardFile(board.getBoardFile());
			boardDTO.setViewCount(board.getViewCount());
			boardDTO.setSectionNo(board.getSection().getSectionNo());
			
			return boardDTO;
			
		}
		
		return null;
	}
	
	@Override
	public void videoDetailCount(long boardNo) {
		
	    Optional<Board> result = br.findByBoardNoAndSection_sectionNo(boardNo, 3L);

	    if(result.isPresent()) {
	        Board board = result.get();
	        board.setViewCount(board.getViewCount() + 1);
	        br.save(board);
	    }
		
	}
	
	@Override
	public boolean videoWrite(BoardDTO boardDTO) {
		
		LocalDateTime now = LocalDateTime.now();
		
		int LocalDateValue = now.getYear();
		
		try {
			
			Board board = Board.builder().
					boardTitle(boardDTO.getBoardTitle()).
					boardContent(boardDTO.getBoardContent()).
					boardFile(boardDTO.getBoardFile()).
					boardWriteYear(LocalDateValue).
					section(sr.findById(boardDTO.getSectionNo()).orElse(null)).
					build();
			
			board = br.save(board);
			
			return true;
			
		}catch(Exception e) {
			return false;
		}
		
	}
	
	@Override
	public boolean videoModify(BoardDTO boardDTO) {
		
		try {
			
			Board board = Board.builder().
					boardNo(boardDTO.getBoardNo()).
					boardTitle(boardDTO.getBoardTitle()).
					boardContent(boardDTO.getBoardContent()).
					boardFile(boardDTO.getBoardFile()).
					boardWriteYear(boardDTO.getBoardWriteYear()).
					viewCount(boardDTO.getViewCount()).
					section(sr.findById(boardDTO.getSectionNo()).orElse(null)).
					build();
			br.save(board);
			
			return true;
			
		}catch(Exception e) {
			return false;
		}

	}
	
	@Override
	public boolean videoRemove(long boardNo) {
		
			if(br.existsById(boardNo)){
				br.deleteById(boardNo);
				return true;
			}else {
				return false;
			}
			
	}
	
}
