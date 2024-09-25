package com.green.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.green.dto.BoardDTO;
import com.green.dto.CheckMessage;
import com.green.dto.PageDTO;
import com.green.dto.PagingInfo;
import com.green.service.ArchiveService;


@Controller
@RequestMapping("/archive")
public class ArchiveController {
	
	@Autowired
	ArchiveService service;
	
	@GetMapping("/mainArchive")
	public ResponseEntity<Map<String, Object>> goToMainArchiveSection(){
		Map<String, Object> response = new HashMap<>();
		response.put("archiveList", service.getMainArchiveList());
		return ResponseEntity.ok(response);
		
	}

	@GetMapping("/archiveAllList")
	public ResponseEntity<Map<String, Object>> goToArchive(PagingInfo pagingInfo) {
		Map<String, Object> response = new HashMap<>();
		long total = service.getArchiveCount(pagingInfo);
		
		response.put("list", service.getArchiveList(pagingInfo));
		response.put("maxDate", service.getMaxDate());
		response.put("minDate", service.getMinDate());
		response.put("page", new PageDTO(pagingInfo, total));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getArchiveListByYear")
	public ResponseEntity<Map<String, Object>> goToArchiveOne(PagingInfo pagingInfo, @RequestParam("thisYear") int thisYear) {
		Map<String, Object> response = new HashMap<>();

		long total = service.getArchiveCountByAnotherDate(thisYear);
		
		response.put("list", service.getArchiveListByAnotherDate(pagingInfo, thisYear));
		response.put("maxDate", service.getMaxDate());
		response.put("minDate", service.getMinDate());
		response.put("page", new PageDTO(pagingInfo, total));
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/detail/{boardNo}")
	public ResponseEntity<BoardDTO> archiveView(@PathVariable("boardNo") long boardNo, BoardDTO boardDTO) {
		
		BoardDTO response = new BoardDTO();
		
		service.archiveDetailCount(boardNo);
		response = service.getArchiveDetail(boardNo, boardDTO);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/write")
	public ResponseEntity<CheckMessage> writeArchive(BoardDTO boardDTO) {
		boolean writeCheck = service.archiveWrite(boardDTO);
		
		return writeCheck
		? ResponseEntity.ok(new CheckMessage("작성 됐습니다.", true))
		: ResponseEntity.ok(new CheckMessage("작성이 되지 않았습니다.", false));
	}
	
	@PutMapping("/modify")
	public ResponseEntity<CheckMessage> archiveModify(BoardDTO boardDTO){
		boolean modifyCheck = service.archiveModify(boardDTO);
		
		return modifyCheck
		? ResponseEntity.ok(new CheckMessage("수정 됐습니다.", true))
		: ResponseEntity.ok(new CheckMessage("수정되지 않았습니다.", false));
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity<CheckMessage> archiveRemove(long boardNo) {
		boolean removeCheck = service.archiveRemove(boardNo);
		
		return removeCheck
		? ResponseEntity.ok(new CheckMessage("삭제 됐습니다.", true))
		: ResponseEntity.ok(new CheckMessage("삭제가 되지 않았습니다.", false));
	}

}
