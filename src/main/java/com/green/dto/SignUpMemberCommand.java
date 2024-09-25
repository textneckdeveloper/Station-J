package com.green.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.green.security.validation.ValidationGroups.AssertTrueGroup;
import com.green.security.validation.ValidationGroups.NotBlankGroup;
import com.green.security.validation.ValidationGroups.PatternGroup;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpMemberCommand {
	
	// 폼으로부터온 데이터를 사용할 커맨드객체
	@NotBlank(message = "닉네임은 필수정보입니다.", groups=NotBlankGroup.class)
	@Pattern(regexp = "^[가-힣a-zA-Z0-9]+$", message = "닉네임은 한글, 영문자, 숫자로만 구성해주세요.", groups=PatternGroup.class)
	private String nickName;
	
	@NotBlank(message = "아이디는 필수정보입니다.", groups=NotBlankGroup.class)
	@Pattern(regexp = "^[a-z]+[a-z0-9]{1,12}", message = "아이디는 영소문자, 숫자로만 입력해주세요.", groups=PatternGroup.class)
	private String id;
	

	@NotBlank(message = "비밀번호는 필수정보입니다.", groups=NotBlankGroup.class)
	@Pattern(regexp = "^(?=.*\\S+$)(?=.*[\\W_]).{8,16}$", message = "비밀번호는 8~16자, 특수문자를 반드시 포함해야 합니다.", groups=PatternGroup.class)
	private String password;
	
	@NotBlank(message = "이메일은 필수정보입니다.", groups=NotBlankGroup.class)
	@Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "올바른 이메일 형식이 아닙니다.", groups=PatternGroup.class)
	private String email;
	
	private boolean agreement;
	@AssertTrue(message = "약관에 동의해야 합니다.", groups=AssertTrueGroup.class)
	public boolean isAgreement() {
	    return this.agreement;
	}
	
	private boolean isEmailVerificated;
	@JsonProperty("isEmailVerificated")
	@AssertTrue(message = "이메일 인증을 완료해야 합니다.", groups=AssertTrueGroup.class)
	public boolean isEmailVerificated() {
	    return this.isEmailVerificated;
	}
}
