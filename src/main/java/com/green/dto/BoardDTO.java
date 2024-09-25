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
