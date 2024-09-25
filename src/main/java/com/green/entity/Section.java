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
	
	@Column(length=50, nullable=false)
	private String sectionName;

	public Section(String sectionName) {
		this.sectionName = sectionName;
	}
	
}
