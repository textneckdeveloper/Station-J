package com.green.service;

import java.util.List;

import com.green.dto.BoardDTO;
import com.green.dto.PagingInfo;

public interface ArchiveService {

	public List<BoardDTO> getMainArchiveList();
	
	public int getMaxDate();
	public int getMinDate();
	
	public List<BoardDTO> getArchiveList(PagingInfo pagingInfo);
	public List<BoardDTO> getArchiveListByAnotherDate(PagingInfo pagingInfo, int year);
	
	public int getArchiveCount(PagingInfo pagingInfo);
	public int getArchiveCountByAnotherDate(int year);
	
	public BoardDTO getArchiveDetail(long boardNo, BoardDTO boardDTO);
	public void archiveDetailCount(long boardNo);
	
	public boolean archiveWrite(BoardDTO boardDTO);
	public boolean archiveModify(BoardDTO boardDTO);
	public boolean archiveRemove(long boardNo);
	
}
