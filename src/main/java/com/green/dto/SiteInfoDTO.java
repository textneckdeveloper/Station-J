package com.green.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SiteInfoDTO {
	private int totalContents;
	private int totalArchiveContents;
	private int totalVideoContents;
	private int totalMembers;
	private int totalMembersFromSocial;
}
