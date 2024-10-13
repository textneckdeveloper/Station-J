<div align="center">
  <img src="https://github.com/user-attachments/assets/6c7cb64b-4c2c-484c-822f-ecf840de1bb5">  <br><br>
  <p>기업으로부터 수주를 받아 진행된 정부 지원 프로젝트입니다.</p>
  <p>인천 광역시의 문화재를 알리기 위한 취지로, 커뮤니티 형식의 Prototype으로 제작되었습니다.</p> <br><br>
</div>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>개발 기간 :</strong> 2024.04.01 ~ 2024.05.08<br><br>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>개발 인원 :</strong> 4명<br><br>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>담당 역할 :</strong> Restful API를 기반으로 CRUD 구현, JPA를 이용한 페이징과 검색 기능 구현<br><br>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>언어 :</strong> Java (JDK 17.0.9), HTML/CSS, JavaScript<br><br>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>서버 :</strong> Apache Tomcat 9.0<br><br>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>프레임워크 :</strong> Spring Boot 3.2.4<br><br>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>DB :</strong> PostgreSQL<br><br>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>IDE :</strong> Spring Tool Suite 4.21.1, Postman, HeidiSQL, Visual Studio Code<br><br>
&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp&nbsp
<strong>API, 라이브러리 :</strong> Restful API (JSON), JPA, React<br><br><br><br><br><br><br><br>

[ SPRING BOOT ]<br><br>

● Config Package<br><br>

1. WebMvcConfigurer.java
~~~java
package com.green.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
public class WebMvcConfigurer implements org.springframework.web.servlet.config.annotation.WebMvcConfigurer {

	 @Override
	    public void addResourceHandlers(ResourceHandlerRegistry registry) {
	        registry.addResourceHandler("/asset/**")
	                .addResourceLocations("file:///C:/asset/");
	    }
	 
}
~~~

클라이언트가 웹 애플리케이션에서 /asset/**로 시작하는 경로에 접근할 때,<br>
애플리케이션은 서버의 로컬 디스크에 있는 C:/asset/ 폴더의 파일을 정적 자원으로 제공합니다.

<br>

● Dto Package<br><br>

1. BoardDTO.java
~~~java
package com.green.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BoardDTO {
	
		private long boardNo;
		private String boardTitle;
		private String boardContent;
		private String boardFile;
		private MultipartFile file;
		private int viewCount;
		private int BoardWriteYear;
		private LocalDateTime regDate;
		private LocalDateTime modDate;
		private Long sectionNo;
		
}
~~~

게시글과 관련된 데이터를 저장, 관리, 그리고 전송하기 위해 객체를 생성합니다.

<br>

2. PageDTO.java
~~~java
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
		
		if (realEndPage < this.endPage) {
			this.endPage = realEndPage;
		}
		
		this.prev = startPage != 1;
		this.next = this.endPage < realEndPage;
		
	}
	
}
~~~

게시판에 데이터가 많으면 페이지 번호를 표시하고,<br>
사용자가 이전/다음 버튼을 통해 쉽게 다른 페이지로 이동할 수 있도록 하는 페이징 기능을 구현했습니다.

<br>

3. PagingInfo.java
~~~java
package com.green.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PagingInfo {
	
	private int pageNum;
	private int amount;
	
	public PagingInfo() {
		this(1, 9);
	}
	
}
~~~

이 클래스는 페이징 처리에서 현재 몇 번째 페이지인지와 한 페이지에 몇 개의 데이터가 표시되는지를 관리합니다.

<br>

● Entity Package<br><br>

1. Board.java
~~~java
package com.green.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="board")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class Board extends BaseEntity {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long boardNo;
	
	@Column
	private int boardWriteYear;
	
	@Column(length = 100, nullable = false)
	private String boardTitle;
	
	@Column(length = 1000, nullable = false)
	private String boardContent;
	
	@Setter
	@Column
	private String boardFile;
	
	@Setter
	@Column(nullable = false)
	private int viewCount;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "section_no", nullable = false)
	private Section section;
	
}
~~~

이 클래스는 게시글 정보를 데이터베이스의 board 테이블과 매핑하여 관리하는 엔티티이며,<br>
JPA를 통해 데이터베이스의 CRUD(생성, 읽기, 수정, 삭제) 연산을 쉽게 수행할 수 있도록 설계되었습니다.<br><br>

section 필드는 다대일(Many-to-One) 관계를 설정합니다.<br>
이 설정은 여러 개의 게시글(Board)이 하나의 섹션(Section)에 속할 수 있음을 의미합니다.

<br>

2. Section.java
~~~java
package com.green.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name="section")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class Section {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "section_seq")
	@SequenceGenerator(name = "section_seq", sequenceName = "SECTION_SEQ", allocationSize = 1)
	private Long sectionNo;
	
	@Column(length = 50, nullable = false)
	private String sectionName;

	public Section(String sectionName) {
		this.sectionName = sectionName;
	}
	
}
~~~

JPA를 사용하여 데이터베이스의 section 테이블과 매핑된 엔티티 클래스입니다.<br><br>

Section 클래스는 게시글이 속할 섹션(카테고리)의 정보를 표현합니다.<br>
이를 통해 게시글을 카테고리별로 분류하고 관리할 수 있습니다.

<br>

● Repository Package<br><br>

1. BoardRepository.java
~~~java
package com.green.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.green.entity.Board;

public interface BoardRepository extends JpaRepository<Board, Long> {
	
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
~~~

Spring Data JPA를 사용하여 Board 엔티티에 대한 데이터베이스 작업을 정의한 BoardRepository 인터페이스입니다.<br><br>

이 인터페이스는 JpaRepository를 상속받아 기본 CRUD 기능을 제공하며,<br>
추가적으로 다양한 사용자 정의 쿼리를 정의하고 있습니다.

<br>

● Controller Package<br><br>

1. MainController.java
~~~java
package com.green.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.green.dto.MemberInfoDTO;
import com.green.dto.PageDTO;
import com.green.dto.PagingInfo;
import com.green.dto.ResponseMessage;
import com.green.security.dto.AuthMemberDetails;
import com.green.service.MainService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value={"/", "/main"})
@RequiredArgsConstructor
public class MainController {

	private final MainService service;
	
	@GetMapping("/api/csrf-token")
	public ResponseEntity<Map<String, String>> getCsrfToken(HttpServletRequest request) {
		
	    CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
	    Map<String, String> tokens = new HashMap<>();
	    tokens.put("token", csrf.getToken());
	    return ResponseEntity.ok(tokens);
	    
	}
	
	@GetMapping("/userAuthentication")
	public ResponseEntity<ResponseMessage> getUserAuth(@AuthenticationPrincipal AuthMemberDetails authMember) {
		
		if(authMember != null) {
			MemberInfoDTO memberInfo = MemberInfoDTO.builder()
					.id(authMember.getId())
					.nickName(authMember.getNickName())
					.email(authMember.getEmail())
					.roleSet(authMember.getAuthorities().stream().map(auth->auth.getAuthority()).collect(Collectors.toSet()))
					.build();
			
			if(authMember.getNickName() == null) {
				return ResponseEntity.ok().body(new ResponseMessage("추가정보 입력이 필요합니다.", null, false));
			}
			return ResponseEntity.ok().body(new ResponseMessage(memberInfo, true));
		} return ResponseEntity.ok().body(null);
		
	}
	
	@GetMapping("/search")
	public ResponseEntity<Map<String, Object>> search(@RequestParam("searchQuery") String searchQuery, PagingInfo pagingInfo) {
		
		long total = service.getSearchBoardCount(searchQuery);
		
		Map<String, Object> response = new HashMap<>();
		
		response.put("board", service.boardSearch(pagingInfo, searchQuery));
		response.put("page", new PageDTO(pagingInfo, total));
		
		return ResponseEntity.ok(response);
		
	}
	
}
~~~

이 클래스는 메인 페이지에 대한 HTTP 요청을 처리하고,<br>
사용자 인증 정보, CSRF 토큰, 게시판 검색 등의 기능을 제공합니다. 

<br>

2. ArchiveController.java
~~~java
package com.green.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
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
	public ResponseEntity<Map<String, Object>> goToMainArchiveSection() {
		
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
	public ResponseEntity<CheckMessage> archiveModify(BoardDTO boardDTO) {
		
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
~~~

아카이브와 관련된 CRUD(Create, Read, Update, Delete) 기능을 처리하는 RESTful API의 컨트롤러입니다.<br><br>

다양한 HTTP 메서드(GET, POST, PUT, DELETE)를 사용하여<br>
아카이브 목록 조회, 연도별 목록 조회, 세부 정보 조회, 작성, 수정, 삭제 기능을 제공합니다.<br><br>

ArchiveService를 통해 비즈니스 로직을 처리하고, 결과를 JSON 형식으로 클라이언트에 반환합니다.

<br>

3. VideoController.java
~~~java
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
	public ResponseEntity<Map<String, Object>> goToMainVideoSection() {
		
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
~~~

비디오와 관련된 CRUD(Create, Read, Update, Delete) 기능을 처리하는 RESTful API의 컨트롤러입니다.<br><br>

다양한 HTTP 메서드(GET, POST, PUT, DELETE)를 사용하여<br>
비디오 목록 조회, 세부 정보 조회, 작성, 수정, 삭제 기능을 제공합니다.<br><br>

VideoService 통해 비즈니스 로직을 처리하고, 결과를 JSON 형식으로 클라이언트에 반환합니다.

<br>

● Service Package<br><br>

Service Class는 비즈니스 로직을 구현하기 위한 메서드를 정의합니다.<br>
Service Interface는 애플리케이션의 구조를 명확하게 하고, 유지보수를 용이하게 합니다.<br><br>

ServiceImpe Class는 인터페이스에서 정의된 메서드를 실제로 실행하는 역할을 합니다.<br><br>

1. MainService.java
~~~java
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
~~~

<br>

MainServiceImpe.java
~~~java
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
		
		for (Board board : result) {
			
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
		
		if (br.getBoardCount(2L).isPresent()) {
			totalArchiveContents = (Integer)br.getBoardCount(2L).get();
		}
		if (br.getBoardCount(3L).isPresent()) {
			totalVideoContents = (Integer)br.getBoardCount(3L).get();
		}
		
		totalContents = totalArchiveContents + totalVideoContents;
		
		if (mr.getMemberCountBySocial(false).isPresent()) {
			totalMembersNotFromSocial = (Integer)mr.getMemberCountBySocial(false).get();
		}
		if (mr.getMemberCountBySocial(true).isPresent()) {
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
~~~

<br>

[ 주요 기능 ]<br><br>

1. 게시물 검색
2. 검색 된 게시물의 수를 반환하여 페이징을 구현
3. 사이트 정보 집계

<br>

2. ArchiveService.java
~~~java
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
~~~

<br>

ArchiveServiceImpe.java
~~~java
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
		
		for (Board board : result.getContent()) {
			
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
		
		for (Board board : result.getContent()) {
			
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
		
		if (result.isPresent()) {
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

	    if (result.isPresent()) {
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
			
			if (boardDTO.getFile() == null || boardDTO.getFile().isEmpty()) {
				
				br.save(board);
				
			} else {
				
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
			
			if (boardDTO.getBoardFile().equals("null")) {
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
	        
	        if (boardDTO.getFile() == null || boardDTO.getFile().isEmpty()) {
	        		
	        	br.save(board);
	        		
	        } else {
	        		
	        	if (imageFile.exists()) {
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

		if (result.isPresent()) {
			
			Board board = result.get();
				
			String uploadDir = "C:/asset/";
				
			String imagePath = uploadDir + board.getBoardFile();
		        
			File imageFile = new File(imagePath);
			
			if (br.existsById(boardNo)) {
		        	
				if (imageFile.exists()) {
		        	imageFile.delete();
		        }
		        	
		        br.deleteById(boardNo);
		        	
		        return true;
		        	
			}

		}
			
		return false;
		
	}

}
~~~

<br>

[ 주요 기능 ]<br><br>

1. 특정 아카이브의 작성일을 조회하여 연도별로 카테고리화
2. 아카이브 게시물 목록 조회
3. 아카이브 게시물의 총 개수 조회
4. 아카이브 게시물 세부 정보 조회
5. 게시물 조회수 증가
6. 게시물 작성
7. 게시물 수정
8. 게시물 삭제

<br>

3. VideoService.java
~~~java
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
~~~

<br>

VideoServiceImpe.java
~~~java
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
public class VideoServiceImpe implements VideoService {
	
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
		
		for (Board board : result.getContent()) {
			
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
		
		for (Board board : result.getContent()) {
			
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
		
		if (result.isPresent()) {
			
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

	    if (result.isPresent()) {
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
		
		if (br.existsById(boardNo)){
			br.deleteById(boardNo);
			return true;
		} else {
			return false;
		}
	
	}
	
}
~~~

<br>

[ 주요 기능 ]<br><br>

1. 비디오 게시물 목록 조회
2. 비디오 게시물의 총 개수 조회
3. 비디오 세부 정보 조회
4. 게시물 조회수 증가
6. 게시물 작성
7. 게시물 수정
8. 게시물 삭제

<br>
<hr>
<br>

[ REACT ]<br><br>

API와의 상호작용을 위해 Axios를 이용해 JSON 데이터를 송수신했습니다.<br>
아래의 코드들은 React로 작성된 프로젝트의 일부분입니다.<br><br>

● Archive<br><br>

1. ArchiveBoardSection.js
~~~jsx
import React, { useState, useEffect } from "react";
import { useNavigate } from 'react-router-dom'; 
import axios from "axios";

const ArchiveBoardSection = () => {
    const navigate = useNavigate();

    const [vrList, setVrList] = useState([]);
    const [maxDate, setMaxDate] = useState(0);
    const [minDate, setMinDate] = useState(0);

    const [yearsRange, setYearsRange] = useState([]);
    const [selectYear, setSelectYear] = useState(2020);
    const [activeFilter, setActiveFilter] = useState('all');


    const [page, setPage] = useState({
        startPage: 1,
        endPage: 0,
        prev: false,
        next: false,
        total: 0,
        pagingInfo : {
            pageNum: 1,
            amount: 9
        }
        
    });

    useEffect(() => {
        fetchData(1);
    }, [selectYear,activeFilter]);

    // 연도 범위 생성
    useEffect(() => {
        const years = [];
        for (let i = minDate; i <= maxDate; i++) {
            years.push(i.toString());
        }
        setYearsRange(years);
    }, [maxDate]);
    

    // 데이터 로드 
    const fetchData = (pageNum) => {
        let url = activeFilter === 'all'
            ? '/archive/archiveAllList'
            : `/archive/getArchiveListByYear?thisYear=${selectYear}`;
        axios.get(url, { params: { pageNum } })
            .then(response => {
                const { data } = response;
                updatePageData(data, pageNum);              
            })
            .catch(error => {
                console.error('Error fetching data:', error);
            });
    };

    // 페이지 데이터 업데이트
    const updatePageData = (data, pageNum) => {

        setPage({
            startPage: data.page.startPage,
            endPage: data.page.endPage,
            prev: data.page.prev,
            next: data.page.next,
            total: data.page.total,
            pagingInfo:{
                pageNum: pageNum,
                amount: 9
            }
        });
        setVrList(data.list);
        setMaxDate(data.maxDate);
        setMinDate(data.minDate);
    };

    // 연도 선택 핸들러
    const handleYearClick = (yearClicked) => {
        setSelectYear(yearClicked);
        setActiveFilter('year');
        setPage(prevState => ({
            ...prevState,
            pagingInfo: {
                ...prevState.pagingInfo,
                pageNum: 1
            }
        }));          
    };

    // 아카이브 상세 페이지로 이동
    const archiveDetailClick = (boardNo) => {
        navigate(`/board/archive/detail/${boardNo}`, { state: { boardNo } });
    };

    return (
        <>
            <section className="section">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-12 filters-group-wrap">
                            <div className="filters-group">
                                <ul className="container-filter list-inline mb-0 filter-options text-center">
                                    <li className={`list-inline-item categories-name border text-dark rounded ${activeFilter === 'all' ? 'active' : ''}`}
                                        onClick={() => { setActiveFilter('all') }} data-group="all">All</li>
                                    {yearsRange.map(year => (
                                        <li className={`list-inline-item categories-name border text-dark rounded ${selectYear === year && activeFilter === 'year' ? 'active' : ''}`}
                                            onClick={() => {handleYearClick(year)}}
                                            key={year}>{year}
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    </div>
                    <div id="grid" className="row">
                        {vrList.map(vr => (
                            <div className="col-lg-4 col-md-6 col-12 mt-4 pt-2 picture-item" key={vr.boardNo}>
                                <div className="card blog border-0 work-container work-primary work-classic shadow rounded-md overflow-hidden">
                                    <img src={vr.boardFile ? `/asset/${vr.boardFile}` : `/asset/noimage.jpg`} className="img-fluid work-image" alt="" style={{ width: '100%', height: 'auto', aspectRatio: '356 / 267' }}/>
                                    <div className="card-body" style={{margin:0}}>
                                        <div className="content">
                                            <a href="#" className="badge badge-link bg">{vr.boardWriteYear}</a>
                                            <h5 className="mt-3"><a href="#" className="text-dark title">{vr.boardTitle}</a></h5>
                                            <p className="text-muted" style={{ display: '-webkit-box', WebkitLineClamp: '2', WebkitBoxOrient: 'vertical', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'normal', maxWidth: '100%' }}>{vr.boardContent}</p>
                                            <a className="link h6" 
                                                style={{ cursor: 'pointer' }} 
                                                onClick={() => archiveDetailClick(vr.boardNo)}>
                                                Read More <i className="uil uil-angle-right-b align-middle"></i>
                                            </a>
                                        </div>                                        
                                    </div>

                                       <div className="author">
                                        <small className="date"><i className="uil uil-calendar-alt"></i> {new Date(vr.regDate).toLocaleDateString()}</small>
                                    </div>                                 
                                </div>
                            </div>                            
                        ))}
                    </div>
                </div>
                
                <div className="col-12 mt-4">
                    <ul className="pagination justify-content-center mb-0">
                        {page.prev && (
                            <li className="page-item"><button className="page-link" onClick={() => page.prev && fetchData(page.startPage - 1)} aria-label="Previous">Prev</button></li>
                        )}
                        {Array.from({ length: (page.endPage - page.startPage + 1) }, (_, i) => page.startPage + i).map(pageNum => (
                            <li className={`page-item ${pageNum === page.pagingInfo.pageNum ? 'active' : ''}`} key={pageNum}>
                                <button className="page-link" onClick={() => fetchData(pageNum)}>{pageNum}</button>
                            </li>
                        ))}
                        {page.next && (
                            <li className="page-item"><button className="page-link" onClick={() => page.next && fetchData(page.endPage + 1)} aria-label="Next">Next</button></li>
                        )}
                    </ul>
                </div>
            </section>
        </>
    );
}

export default ArchiveBoardSection;
~~~

<br>

2. ArchiveDetailSection.js
~~~jsx
import React, { useEffect, useState } from "react";
import { useLocation } from 'react-router-dom';
import { useParams } from "react-router-dom";

const ArchiveDetailSection = () => {
    let {boardNo} = useParams();

    const [board, setBoard] = useState({
        boardNo: 0,
        boardTitle: "",
        boardContent: "",
        boardFile: ""
    });

    useEffect(() => {
        if (boardNo) {
            fetch(`http://localhost:8080/archive/detail/${boardNo}`)
                .then(response => response.json())
                .then(data => {
                    setBoard({
                        boardNo: data.boardNo,
                        boardTitle: data.boardTitle,
                        boardContent: data.boardContent,
                        boardFile: data.boardFile
                    });
                })
                .catch(error => console.log('Error fetching data:', error));
        }
    }, [boardNo]); // boardNo를 종속성 배열에 추가

    return(
        <>
            <section className="bg-half">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-lg-8 col-md-10">
                            <div className="section-title">
                                <div className="text-center">
                                    <h4 className="title mb-4">{board.boardTitle}</h4> 
                                    <div id="panorama" 
                                        style={{
                                            width:"100%", 
                                            height: 'auto',
                                            aspectRatio: '356 / 267',                                      
                                            backgroundSize: "cover",
                                            backgroundPosition: "center",
                                            backgroundImage: board.boardFile ? `url(/asset/${board.boardFile})` : `url(/asset/noimage.jpg)`
                                        }}></div>
                                </div>
                                <p className="text-muted mb-0 mt-4">{board.boardContent}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </>
    );
}

export default ArchiveDetailSection;
~~~

<br>

● Video<br><br>

1. VideoBoardSection.js
~~~jsx
import React, { useState } from "react";
import { useEffect } from "react";
import { useNavigate } from "react-router-dom"; 
import axios from "axios";

const VideoSection = () => {

    const navigate = useNavigate();


    const videoDetailClick = (boardNo) => {
        navigate(`/board/video/detail/${ boardNo }`, { state: { boardNo } });
    };

    const [page, setPage] = useState({
        startPage: 1,
        endPage: 0,
        prev: false,
        next: false,
        total: 0,
        pagingInfo: {
            pageNum: 1,
            amount: 0
        }
    });


    const [VideoList, setVideoList] = useState([]);

    useEffect(() => {
        axiosPageData(page.pagingInfo.pageNum);
    }, []);

    const axiosPageData = (pageNum) => {

        const amount = 9;

        axios.get('/video/videoAllList?', {
            params : {
                pageNum : pageNum,
                amount : amount
            }
        })
            .then(response => {
                const { data } = response;
                setPage({
                    startPage: data.page.startPage,
                    endPage: data.page.endPage,
                    prev: data.page.prev,
                    next: data.page.next,
                    total: data.page.total,
                    pagingInfo: {
                        pageNum: data.page.pagingInfo.pageNum,
                        amount: data.page.pagingInfo.amount
                    }
                });
                setVideoList(data.list);
            })
            .catch(error => {
                console.log('Error fetching data:', error);
                console.error(error.response);
            });
    };






    function getYouTubeEmbedUrl(url) {
        const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=)([^#\&\?]*).*/;
        const match = url.match(regExp);
      
        if (match && match[2].length === 11) {          
          return `https://img.youtube.com/vi/${match[2]}/0.jpg`;
        }
      
        return url; 
    }


    return (
        <section className="section">
            <div className="container">
                <div className="row">
                    {VideoList.map(videoList => (
                        <div className="col-lg-6 col-12 mb-4 pb-2" key={videoList.boardNo}>
                            <div className="card blog blog-primary rounded border-0 shadow overflow-hidden">
                                <div className="row align-items-center g-0">
                                    <div className="col-md-6">
                                        <img className="img-fluid" alt=""
                                            width="100%"
                                            src={getYouTubeEmbedUrl(videoList.boardFile)}
                                        ></img>
                                        <div className="overlay"></div>
                                        <div className="author">
                                            <small className="date"><i className="uil uil-calendar-alt"></i> {new Date(videoList.regDate).toLocaleDateString()}</small>
                                        </div>
                                    </div>
                                    <div className="col-md-6">
                                        <div className="card-body content">
                                            <h5><a href="" className="card-title title text-dark" onClick={()=> videoDetailClick(videoList.boardNo)}>{videoList.boardTitle}</a></h5>
                                            <p className="text-muted mb-0"                                            
                                                style={{
                                                    display: '-webkit-box',
                                                    WebkitLineClamp: '2',
                                                    WebkitBoxOrient: 'vertical',
                                                    overflow: 'hidden',
                                                    textOverflow: 'ellipsis',
                                                    whiteSpace: 'normal',
                                                    maxWidth: '100%' 
                                                }}>                                           
                                            
                                            {videoList.boardContent}</p>
                                            <div className="post-meta d-flex justify-content-between mt-3">
                                                <ul className="list-unstyled mb-0">
                                                    <li className="list-inline-item me-2 mb-0"><a className="text-muted like"><i className="uil uil-eye me-1"></i>{videoList.viewCount}</a></li>
                                                </ul>
                                                <a className="text-muted readmore" style={{cursor: "pointer"}} onClick={()=> videoDetailClick(videoList.boardNo)}>Read More <i className="uil uil-angle-right-b align-middle"></i></a>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div> 
                    ))}
                </div>

                {/* <!-- PAGINATION START --> */}
                <div className="col-12">
                    <ul className="pagination justify-content-center mb-0">
                        <li className="page-item"><a className="page-link" onClick={()=>{ page.prev && axiosPageData(page.startPage-1)}} style={{ cursor: 'pointer' }} aria-label="Previous">Prev</a></li>
                            {Array.from({ length: (page.endPage - page.startPage + 1) }, (_, i) => page.startPage + i).map(pageNum => (
                            <li className={`page-item ${pageNum === page.pagingInfo.pageNum ? 'active' : ''}`} key={pageNum}><a className="page-link" style={{ cursor: 'pointer' }} onClick={() => {axiosPageData(pageNum)}}>{pageNum}</a></li>
                            ))}
                        <li className="page-item"><a className="page-link" onClick={()=>{ page.next && axiosPageData(page.endPage+1)}} style={{ cursor: 'pointer' }} aria-label="Next">Next</a></li>
                    </ul>
                </div>{/* <!--end col--> */}
                {/* <!-- PAGINATION END -- */}



            </div>{/* container */}
      </section> //section
    );
}
export default VideoSection;
~~~

<br>

2. VideoDetailSection.js
~~~jsx
import React from "react";
import { useParams } from "react-router-dom";
import { useState } from "react";
import { useEffect } from "react";

const VideoDetailSection = () => {
    let {boardNo} = useParams();

    const [board, setBoard] = useState({
        boardNo: 0,
        boardTitle: "",
        boardContent: "",
        boardFile: ""
    });

    useEffect(() => {
        if (boardNo) {
            fetch(`http://localhost:8080/video/detail/${boardNo}`)
                .then(response => response.json())
                .then(data => {

                    const videoId = new URL(data.boardFile).searchParams.get("v");
                    const embedUrl = `https://www.youtube.com/embed/${videoId}`;

                    setBoard({
                        boardNo: data.boardNo,
                        boardTitle: data.boardTitle,
                        boardContent: data.boardContent,
                        boardFile: embedUrl
                    });
                })
                .catch(error => console.log('Error fetching data:', error));
        }
    }, []); 


    return(
        <section className="bg-half">
            <div className="container">
                <div className="row justify-content-center">
                    <div className="col-lg-8 col-md-10">
                        <div className="section-title">
                            <div className="text-center">
                                <h4 className="title mb-4">{board.boardTitle}</h4>
                                <iframe width="100%" height="500px" src={board.boardFile} title="YouTube video player" frameBorder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" referrerPolicy="strict-origin-when-cross-origin" allowFullScreen></iframe>
                            </div>
                            <p className="text-muted mb-0 mt-4">{board.boardContent}</p>
                        </div>
                    </div>
                </div>
            </div>                          
        </section>            
    )
}

export default VideoDetailSection;
~~~

<br>

● Search<br><br>

1. SearchBoardSection.js
~~~jsx
import React, {useEffect, useState} from 'react';
import axios from 'axios';
import { useLocation } from 'react-router-dom';
import { useNavigate } from "react-router-dom"; 
import { useParams } from 'react-router-dom';

const SearchBoardSection = (searchInfo, pageNum) => {
    const navigate = useNavigate();
    let [boardList, setBoardList] = useState({list:[]});

    let {searchQuery} = useParams();

    const boardDetailClick = (board) => {
        let boardNo = board.boardNo;
        switch(board.sectionNo){
            case 2 : 
                navigate(`/board/archive/detail/${ boardNo }`, { state: { boardNo } });
                break;
            case 3 : 
                navigate(`/board/video/detail/${ boardNo }`, { state: { boardNo } });
                break;
        }
    }

    const imgSpy = (sectionNo) => {
        switch(sectionNo){
            case 2 : 
                return true;
            case 3 : 
                return false;
            case 4 :
                return true;
        }
    }

    function getYouTubeEmbedUrl(url) {
        const regExp = /^.*(youtu.be\/|v\/|u\/\w\/|embed\/|watch\?v=)([^#\&\?]*).*/;
        const match = url.match(regExp);
      
        if (match && match[2].length === 11) {          
          return `https://img.youtube.com/vi/${match[2]}/0.jpg`;
        }
        return url; 
    }

    let [page, setPage] = useState({
        startPage: 1,
        endPage: 0,
        prev: false,
        next: false,
        total: 0,
        pagingInfo: {
          pageNum: 1,
          amount: 9
        }
      });

    useEffect(() => {
        axiosPageData(page.pagingInfo.pageNum);
    }, []);

    const axiosPageData = (pageNum) => {

        const amount = 9;

        axios.get('/search', {
            params : {
                searchQuery : searchQuery,
                pageNum : pageNum,
                amount : amount
            }
        })
            .then(response => {
                console.log(response.data);
                setPage({
                    startPage: response.data.page.startPage,
                    endPage: response.data.page.endPage,
                    prev: response.data.page.prev,
                    next: response.data.page.next,
                    total: response.data.page.total,
                    pagingInfo: {
                        pageNum: response.data.page.pagingInfo.pageNum,
                        amount: response.data.page.pagingInfo.amount
                    }
                });
                setBoardList({list : response.data.board});
            })
            .catch(error => {
                console.log('Error data:', error);
            });
    };

    return(
        <>
            <section className="section">
                <div className="container">
                    <div className="row justify-content-center">
                        <div className="col-12 filters-group-wrap">
                            <div className="filters-group">
                            </div>
                        </div>
                    </div>
                    <div id="grid" className="row">
                        {page.total!=0 ? boardList.list.map(board => (
                            <div className="col-lg-4 col-md-6 col-12 mt-4 pt-2 picture-item" key={board.boardNo}>
                                <div className="card blog border-0 work-container work-primary work-classic shadow rounded-md overflow-hidden">             

                                { imgSpy(board.sectionNo) ? (
                                    <img src={board.boardFile ? `/asset/${board.boardFile}` : `/asset/noimage.jpg`} className="img-fluid work-image" alt="" style={{ width: '100%', height: 'auto', aspectRatio: '356 / 267' }}/>
                                ) : (
                                    <img
                                        width="100%"
                                        src={getYouTubeEmbedUrl(board.boardFile)}
                                    ></img>                                    
                                )
                                }
                                    <div className="card-body" style={{margin:0}}>
                                        <div className="content">
                                            <a href=" " className="badge badge-link bg">{board.boardWriteYear}</a>
                                            <h5 className="mt-3"><a className="text-dark title">{board.boardTitle}</a></h5>
                                            <p className="text-muted" style={{ display: '-webkit-box', WebkitLineClamp: '2', WebkitBoxOrient: 'vertical', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'normal', maxWidth: '100%' }}>{board.boardContent}</p>
                                            <a className="text-muted readmore" 
                                                style={{ cursor: 'pointer' }} 
                                                onClick={() => boardDetailClick(board)}>
                                                Read More <i className="uil uil-angle-right-b align-middle"></i>
                                            </a>
                                            {/* <button onClick={()=>{boardDetailClick(board)}}>더보기</button> */}
                                       </div>
                                    </div>
                                </div>
                            </div>
                        )):<div className="text-center" style={{fontWeight:'900'}}>검색결과가 없습니다.</div>}
                    </div>
                </div>
                
                <div className="col-12 mt-4">
                <ul className="pagination justify-content-center mb-0">
                    {page.prev && (
                        <li className="page-item"><a className="page-link" style={{ cursor: 'pointer' }} onClick={()=>{ page.prev && axiosPageData(page.startPage-1)}} aria-label="Previous">Prev</a></li>
                    )}
                        {Array.from({ length: (page.endPage - page.startPage + 1) }, (_, i) => page.startPage + i).map(pageNum => (
                        <li className={`page-item ${pageNum === page.pagingInfo.pageNum ? 'active' : ''}`} style={{ cursor: 'pointer' }} key={pageNum}><a className="page-link" onClick={() => {axiosPageData(pageNum)}}>{pageNum}</a></li>
                        ))}
                    {page.next && (
                        <li className="page-item"><a style={{ cursor: 'pointer' }} className="page-link" onClick={()=>{ page.next && axiosPageData(page.endPage+1)}} aria-label="Next">Next</a></li>
                    )}    
                    
                </ul>
            </div>


            </section>
        </>
    )
}
export default SearchBoardSection;
~~~
