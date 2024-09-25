package com.green.paging;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.green.entity.Member;
import com.green.repository.MemberRepository;

import jakarta.transaction.Transactional;

@SpringBootTest
public class Paging {

	@Autowired
	private MemberRepository mr;
	
	@Test
	@Transactional
	public void memberPageDefault() {
		
		// 1 페이지 10개
		Pageable pageable = PageRequest.of(0, 10);
		Page<Member> result = mr.findAll(pageable);
		System.out.println(result);
		
		System.out.println("==============================================");
		System.out.println("Total Pages : " + result.getTotalPages());
		System.out.println("Total Count : " + result.getTotalElements());
		System.out.println("Page Number: " + result.getNumber());
		System.out.println("Page Size : " + result.getSize());
		System.out.println("has next page ? : " + result.hasNext());
		System.out.println("first page ? : " + result.isFirst());
		System.out.println("==============================================");
		
		for(Member member : result.getContent()) {
			System.out.println(member);
		}
	}
	
	@Test
	@Transactional
	public void testSort() {
		Sort sort1 = Sort.by("regDate").descending();
		Pageable pageable = PageRequest.of(0, 10, sort1);
		Page<Member> result = mr.findAll(pageable);
		
		result.get().forEach(member->{
			System.out.println(member);
		});
	}

}























