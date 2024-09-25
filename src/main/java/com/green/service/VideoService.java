package com.green.service;

import java.util.List;

import com.green.dto.BoardDTO;
import com.green.dto.PagingInfo;

public interface VideoService {

	public List<BoardDTO> getMainVideoList();
	
	public List<BoardDTO> getVideoList(PagingInfo pagingInfo);
	public int getVideoCount();
	
	public BoardDTO getVideoDetail(long boardNo, BoardDTO boardDTO);
	public void videoDetailCount(long boardNo);

	public boolean videoWrite(BoardDTO boardDTO);
	public boolean videoModify(BoardDTO boardDTO);
	public boolean videoRemove(long boardNo);
	
}
