package com.green.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.green.entity.MemberRole;

public interface MemberRoleRepository extends JpaRepository<MemberRole, Long>{

}
