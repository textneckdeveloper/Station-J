package com.green.entity;

import jakarta.persistence.Entity;
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
import lombok.ToString;

@Entity
@Table(name = "member_role")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class MemberRole {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "member_num")
    private Member member;
    
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
	
    public MemberRole(Member member, Role role) {
    	this.member = member;
    	this.role = role;
    }
    
    public String toString() {
        return role.toString();
    }
}
