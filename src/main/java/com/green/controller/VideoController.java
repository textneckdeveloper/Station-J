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

import com.green.dto.BoardDTO;
import com.green.dto.CheckMessage;
import com.green.dto.PageDTO;
import com.green.dto.PagingInfo;
import com.green.service.VideoService;

@CrossOrigin
@Controller
@RequestMapping("/video")
public class VideoController {

	@Autowired
	VideoService service;
	
	@GetMapping("/mainVideo")
	public ResponseEntity<Map<String, Object>> goToMainVideoSection(){
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("videoList", service.getMainVideoList());
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/videoAllList")
	public ResponseEntity<Map<String, Object>> goTovideo(PagingInfo pagingInfo) {
		
		Map<String, Object> response = new HashMap<>();
		
		long total = service.getVideoCount();
		
		response.put("list", service.getVideoList(pagingInfo));
		response.put("page", new PageDTO(pagingInfo, total));
		
		return ResponseEntity.ok(response);
		
	}
	
	@GetMapping("/detail/{boardNo}")
	public ResponseEntity<BoardDTO> videoView(@PathVariable("boardNo") Long boardNo, BoardDTO boardDTO) {
		
		BoardDTO response = new BoardDTO();
		
		service.videoDetailCount(boardNo);
		
		response = service.getVideoDetail(boardNo, boardDTO);
		
		return ResponseEntity.ok(response);
		
	}
	
	@PostMapping("/write")
	public ResponseEntity<CheckMessage> writeVideo(BoardDTO boardDTO) {
		
		boolean writeCheck = service.videoWrite(boardDTO);
			
		return writeCheck
		? ResponseEntity.ok(new CheckMessage("작성 됐습니다.", true))
		: ResponseEntity.ok(new CheckMessage("작성이 되지 않았습니다.", false));
		
	}
	
	@PutMapping("/modify")
	public ResponseEntity<CheckMessage> videoModify(BoardDTO boardDTO) {
		
		boolean modifyCheck = service.videoModify(boardDTO);
		
		return modifyCheck
		? ResponseEntity.ok(new CheckMessage("수정 됐습니다.", true))
		: ResponseEntity.ok(new CheckMessage("수정이 되지 않았습니다.", false));
	
	}
	
	@DeleteMapping("/remove")
	public ResponseEntity<CheckMessage> videoRemove(long boardNo) {

		boolean removeCheck = service.videoRemove(boardNo);
		
		return removeCheck
		? ResponseEntity.ok(new CheckMessage("삭제 됐습니다.", true))
		: ResponseEntity.ok(new CheckMessage("삭제가 되지 않았습니다.", false));
		
	}
	
}
