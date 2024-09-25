package com.green.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.green.entity.Member;

public interface MemberRepository extends JpaRepository<Member, Long>{

	// ID로 회원 조회(일반가입회원전용)
	@EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
	@Query("SELECT m FROM Member m WHERE m.id = :id AND m.fromSocial = :social")
	Optional<Member> findById(String id, boolean social);
	
	// Email로 회원 조회(소셜가입회원전용)
	@EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
	@Query("SELECT m FROM Member m WHERE m.email = :email AND m.fromSocial = :social")
	Optional<Member> findByEmail(String email, boolean social);
	
	// Email로 회원 조회(공통)
	@EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
	@Query("SELECT m FROM Member m WHERE m.email = :email")
	Optional<Member> findInfoByEmail(String email);
	
	// 모든 회원 조회
	@EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
	@Query("SELECT m FROM Member m")
	List<Member> findAllMember();
	
	// 중복회원 조회
	@Query("SELECT m FROM Member m WHERE m.email = :email")
	List<Member> findExistMemberByEmail(String email);
	
	@Query("SELECT m FROM Member m WHERE m.num = :num")
	Optional<Member> findByNum(Long num);
	
	@EntityGraph(attributePaths = {"roleSet"}, type = EntityGraph.EntityGraphType.LOAD)
	@Query("SELECT m from Member m where lower(m.nickName) like lower(concat('%', :searchQuery, '%')) or " +
			   "lower(replace(m.nickName, ' ', '')) like lower(replace(concat('%', :searchQuery, '%'), ' ', '')) " +
			   "or lower(m.email) like lower(concat('%', :searchQuery, '%')) or " +
			   "lower(replace(m.email, ' ', '')) like lower(replace(concat('%', :searchQuery, '%'), ' ', ''))")
	Page<Member> getMemberSearchResult(@Param("searchQuery") String searchQuery, Pageable pageable);
	
	@Query("SELECT count(m) FROM Member m WHERE fromSocial =:fromSocial")
	Optional<Integer> getMemberCountBySocial(@Param("fromSocial") Boolean fromSocial);
}
