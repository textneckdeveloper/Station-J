package com.green.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.green.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
	
	@Query("select b from Board b left join b.section s where s.sectionNo =:sectionNo")
	Page<Board> getBoardListPageAll(@Param("sectionNo") Long sectionNo, Pageable pageable);
	
	@Query("select b from Board b left join b.section s where s.sectionNo =:sectionNo and b.boardWriteYear = :boardWriteYear")
	Page<Board> getBoardPageAnotherDate(@Param("sectionNo") Long sectionNo, @Param("boardWriteYear") int boardWriteYear, Pageable pagealbe);
	
	Optional<Board> findByBoardNoAndSection_sectionNo(long boardNo, Long sectionNo);
	
	@Query("select distinct max(b.boardWriteYear) from Board b left join b.section s where s.sectionNo =:sectionNo")
	Optional<Integer> getBoardDateMax(@Param("sectionNo") Long sectionNo);
	
	@Query("select distinct min(b.boardWriteYear) from Board b left join b.section s where s.sectionNo =:sectionNo")
	Optional<Integer> getBoardDateMin(@Param("sectionNo") Long sectionNo);
	
	@Query("select b from Board b where lower(b.boardTitle) like lower(concat('%', :searchResult, '%')) or " +
			   "lower(replace(b.boardTitle, ' ', '')) like lower(replace(concat('%', :searchResult, '%'), ' ', '')) " +
			   "or lower(b.boardContent) like lower(concat('%', :searchResult, '%')) or " +
			   "lower(replace(b.boardContent, ' ', '')) like lower(replace(concat('%', :searchResult, '%'), ' ', ''))")
	Page<Board> getSearchBoardResult(@Param("searchResult") String searchResult, Pageable pageable);
	
	
	@Query("select count(b) from Board b left join b.section s where sectionNo =:sectionNo")
	Optional<Integer> getBoardCount(@Param("sectionNo") Long sectionNo);
	
}
