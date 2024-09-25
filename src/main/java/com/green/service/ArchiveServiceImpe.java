package com.green.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class ArchiveServiceImpe implements ArchiveService{

	@Autowired
	BoardRepository br;
	
	@Autowired
	SectionRepository sr;
	
	@Override
	public int getMaxDate() {
		
		Optional<Integer> result = br.getBoardDateMax(2L);
		
		if(result.isPresent()) {
			Integer board = result.get();
			
			return board;
		}
		
		return 0;
	}
	
	@Override
	public int getMinDate() {
		
		Optional<Integer> result = br.getBoardDateMin(2L);
		
		if(result.isPresent()) {
			Integer board = result.get();
			
			return board;
		}
		
		return 0;
	}
	
	@Override
	public List<BoardDTO> getMainArchiveList() {
		
		Sort sort = Sort.by("regDate").descending();
		
		Pageable pageable = PageRequest.of(0, 3, sort);
		
		Page<Board> result = br.getBoardListPageAll(2L, pageable);
		
		List<BoardDTO> list = new ArrayList<>();
		
		for(Board board : result.getContent()) {
			
			BoardDTO boardDTO = new BoardDTO();
			
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setBoardTitle(board.getBoardTitle());
			boardDTO.setBoardContent(board.getBoardContent());
			boardDTO.setBoardWriteYear(board.getBoardWriteYear());
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
	public List<BoardDTO> getArchiveList(PagingInfo pagingInfo) {
		
		Sort sort = Sort.by("regDate").descending();
		
		Pageable pageable = PageRequest.of(pagingInfo.getPageNum()-1, pagingInfo.getAmount(), sort);
		
		Page<Board> result = br.getBoardListPageAll(2L, pageable);
		
		List<BoardDTO> list = new ArrayList<>();
		
		for(Board board : result.getContent()) {
			
			BoardDTO boardDTO = new BoardDTO();
			
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setBoardTitle(board.getBoardTitle());
			boardDTO.setBoardContent(board.getBoardContent());
			boardDTO.setBoardWriteYear(board.getBoardWriteYear());
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
	public List<BoardDTO> getArchiveListByAnotherDate(PagingInfo pagingInfo, int thisYear) {
		
		Sort sort = Sort.by("regDate").descending();
		
		Pageable pageable = PageRequest.of(pagingInfo.getPageNum()-1, 9, sort);
		
		Page<Board> result = br.getBoardPageAnotherDate(2L, thisYear, pageable);
		
		List<BoardDTO> list = new ArrayList<>();
		
		for(Board board : result.getContent()) {
			
			BoardDTO boardDTO = new BoardDTO();
			
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setBoardTitle(board.getBoardTitle());
			boardDTO.setBoardContent(board.getBoardContent());
			boardDTO.setBoardWriteYear(board.getBoardWriteYear());
			boardDTO.setRegDate(board.getRegDate());
			boardDTO.setModDate(board.getModDate());
			boardDTO.setBoardFile(board.getBoardFile());
			boardDTO.setSectionNo(board.getSection().getSectionNo());
			
			list.add(boardDTO);
			
		}
		
		return list;
		
	}
	
	@Override
	public int getArchiveCount(PagingInfo pagingInfo) {
		
		Pageable pageable = PageRequest.of(0, pagingInfo.getAmount());
		
		Page<Board> result = br.getBoardListPageAll(2L, pageable);
		
		return (int)result.getTotalElements();
		
	}
	
	@Override
	public int getArchiveCountByAnotherDate(int year) {
		
		Pageable pageable = PageRequest.of(0, 9);
		
		Page<Board> result = br.getBoardPageAnotherDate(2L, year, pageable);
		
		return (int)result.getTotalElements();
		
	}
	
	@Override
	public BoardDTO getArchiveDetail(long boardNo, BoardDTO boardDTO) {
		
		Optional<Board> result = br.findByBoardNoAndSection_sectionNo(boardNo, 2L);
		
		if(result.isPresent()) {
			
			Board board = result.get();
			
			boardDTO.setBoardNo(board.getBoardNo());
			boardDTO.setBoardTitle(board.getBoardTitle());
			boardDTO.setBoardContent(board.getBoardContent());
			boardDTO.setBoardWriteYear(board.getBoardWriteYear());
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
	public void archiveDetailCount(long boardNo) {
		
	    Optional<Board> result = br.findByBoardNoAndSection_sectionNo(boardNo, 2L);

	    if(result.isPresent()) {
	        Board board = result.get();
	        board.setViewCount(board.getViewCount() + 1);
	        br.save(board);
	    }
		
	}
	
	@Override
	public boolean archiveWrite(BoardDTO boardDTO) {
		
		LocalDateTime now = LocalDateTime.now();
		
		int LocalDateValue = now.getYear();
	
		String uploadDir = "C:/asset";
		
		try {
		
			Board board = Board.builder().
					boardTitle(boardDTO.getBoardTitle()).
					boardContent(boardDTO.getBoardContent()).
					boardWriteYear(LocalDateValue).
					section(sr.findById(boardDTO.getSectionNo()).orElse(null)).
					build();
			
			if(boardDTO.getFile() == null || boardDTO.getFile().isEmpty()) {
				
				br.save(board);
				
			}else {
				
				Board saveBoard = br.save(board);
				
				saveBoard.setBoardFile(saveBoard.getBoardNo()+"_"+boardDTO.getFile().getOriginalFilename());
				
				Path filePath = Paths.get(uploadDir, saveBoard.getBoardNo()+"_"+boardDTO.getFile().getOriginalFilename());
				Files.write(filePath, boardDTO.getFile().getBytes());
				
				br.save(saveBoard);
				
			}
			
			return true;
			
		}catch(IOException e) {
			return false;
		}

	}
	
	@Override
	public boolean archiveModify(BoardDTO boardDTO) {

		String uploadDir = "C:/asset";

		try {
			
			if(boardDTO.getBoardFile().equals("null")) {
				boardDTO.setBoardFile(null);
			}
			
			Board board = Board.builder().
					boardNo(boardDTO.getBoardNo()).
					boardTitle(boardDTO.getBoardTitle()).
					boardContent(boardDTO.getBoardContent()).
					boardWriteYear(boardDTO.getBoardWriteYear()).
					viewCount(boardDTO.getViewCount()).
					boardFile(boardDTO.getBoardFile()).
					section(sr.findById(boardDTO.getSectionNo()).orElse(null)).
					build();

			String imagePath = "C:/asset/"+boardDTO.getBoardFile();
			
	        File imageFile = new File(imagePath);
	        
	        	if(boardDTO.getFile() == null || boardDTO.getFile().isEmpty()) {
	        		
	        		br.save(board);
	        		
	        	}else {
	        		
	        		if(imageFile.exists()) {
	        			imageFile.delete();
	        		}
	        		
	        		Board saveBoard = br.save(board);
	        		
	        		saveBoard.setBoardFile(saveBoard.getBoardNo()+"_"+boardDTO.getFile().getOriginalFilename());
	        		
					Path filePath = Paths.get(uploadDir, boardDTO.getBoardNo()+"_"+boardDTO.getFile().getOriginalFilename());
					Files.write(filePath, boardDTO.getFile().getBytes());
					
					br.save(saveBoard);
					
	        	}
	        	
	            return true;
		
		}catch(Exception e) {
			return false;
		}	
			
	}
	
	@Override
	public boolean archiveRemove(long boardNo) {
		
			Optional<Board> result = br.findById(boardNo);

			if(result.isPresent()) {
			
				Board board = result.get();
				
				String uploadDir = "C:/asset/";
				
		        String imagePath = uploadDir + board.getBoardFile();
		        
		        File imageFile = new File(imagePath);
			
		        if(br.existsById(boardNo)) {
		        	
		        	if(imageFile.exists()) {
		        		imageFile.delete();
		        	}
		        	
		        	br.deleteById(boardNo);
		        	
		        	return true;
		        	
		        }

			}
			
			return false;
			
	}

}