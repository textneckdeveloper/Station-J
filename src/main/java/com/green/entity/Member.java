package com.green.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="member")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Member extends BaseEntity{
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long num;

	private String id;
	
	private String password;
	
	private String nickName;
	
	private String email;
	
	private boolean fromSocial;
	
	@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
	@Builder.Default
	private Set<MemberRole> roleSet = new HashSet<>();

	public void addRole(Role role) {
		MemberRole memberRole = new MemberRole(this, role);
        for (MemberRole existingMemberRole : this.roleSet) {
            if (existingMemberRole.getRole().equals(role)) {
                System.out.println("이미 부여된 권한입니다.");
                return;
            }
        }
		this.roleSet.add(memberRole);
	}
	public void removeRole(Role role) {
		roleSet.removeIf(r -> r.getRole().equals(role) && r.getMember().equals(this));
	}

}
