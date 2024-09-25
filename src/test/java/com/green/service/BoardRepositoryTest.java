package com.green.service;

import java.time.LocalDate;
import java.util.Random;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.green.entity.Board;
import com.green.entity.Section;
import com.green.repository.BoardRepository;	
import com.green.repository.SectionRepository;

@SpringBootTest
public class BoardRepositoryTest {

	@Autowired
	BoardRepository br;
	
	@Autowired
	SectionRepository sr;
	
	@Test
	public void insertSection() {
		
		for(int i=1; i<=4; i++) {
		
			Section section = null;
			
					switch(i) {
						case 1:
							section = Section.builder().
							sectionName("모든 권한").
							build();
							break;
						case 2:
							section = Section.builder().
							sectionName("VR 아카이빙").
							build();
							break;
						case 3:
							section = Section.builder().
							sectionName("동영상").
							build();
							break;
						case 4:
							section = Section.builder().
							sectionName("디지털 조감도").
							build();
							break;
					}
					
					sr.save(section);
					
		}
	}
	
	@Test
	public void insertBoard() {

			LocalDate now = LocalDate.now();
			
			Random random = new Random();
			
			for(int i=1; i<=500; i++) {
				
				Long randomValue = random.nextLong(3)+2;
				int randomImage = random.nextInt(5)+1;
				int randomWriterDate = random.nextInt(4)+2024;
				
				if(i==1) {
					Board board = Board.builder().
							boardWriteYear(randomWriterDate).
							boardTitle("Station-J").
							boardContent("질문과 답변").
							boardFile("qna.jpg").
							viewCount(0).
							section(sr.findById(2L).orElse(null)).
							build();
					br.save(board);
				}
				
				if(randomValue == 2) {
					Board board = Board.builder().
							boardWriteYear(randomWriterDate).
							boardTitle("archive, 글 제목 "+i).
							boardContent("archive 글 내용 "+i+" 가나다라마바사아자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자").
							boardFile("example_"+randomImage+".jpg").
							viewCount(0).
							section(sr.findById(randomValue).orElse(null)).
							build();
					br.save(board);
				}else if(randomValue == 3) {
					Board board = Board.builder().
							boardWriteYear(randomWriterDate).
							boardTitle("동영상, 글 제목 "+i).
							boardContent("동영상 글 내용 "+i+" 가나다라마바사아자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자자").
							boardFile("https://www.youtube.com/watch?v=XJk16VjZX0k").
							viewCount(0).
							section(sr.findById(randomValue).orElse(null)).
							build();
					br.save(board);
				}else if(randomValue == 4) {
					Board board = Board.builder().
							boardWriteYear(randomWriterDate).
							boardTitle("디지털 조감도, 글 제목 "+i).
							boardContent("글 내용 "+i).
							viewCount(0).
							section(sr.findById(randomValue).orElse(null)).
							build();
					br.save(board);
				}

			}
			
	}

}
