package com.green.service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import com.green.entity.EmailAuthData;
import com.green.entity.Member;
import com.green.repository.AuthCodeRepository;
import com.green.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberSignServiceImpe implements MemberSignService{

	@Override
	public Map<String, String> validateHandling(Errors errors) {
		Map<String, String> validatorResult = new HashMap<>();
		for(FieldError error : errors.getFieldErrors()) {
			String validKeyName = String.format("%sErrorMsg", error.getField());
			validatorResult.put(validKeyName, error.getDefaultMessage());
		}
		return validatorResult;
	}
	
	// 이메일 인증을 위한 서비스
	private String authCodePrefix = null;
	private final AuthCodeRepository acRepository;
	private final MailService mailService;
	@Value("${spring.mail.auth-code-expiration-millis}")
    private long authCodeExpirationMillis;
	
	// 회원가입용 이메일 인증번호 전송
	public boolean sendCodeToEmail(String toEmail, String service) {
		String title = null;
		if(service.equals("signUp")) {
			title = "Station-J 회원가입 이메일 인증번호"; 
			authCodePrefix = "signUp ";
		}else if(service.equals("recover")) {
			title = "Station-J 비밀번호 찾기 인증번호";
			authCodePrefix = "recover ";
		}else {
			return false;
		}
		String authCode = this.createCode();
		mailService.sendEmail(toEmail, title, authCode);
		
		// 인증코드 DB에 저장
		Optional<EmailAuthData> findByEmail = acRepository.findByEmail(authCodePrefix + toEmail);
		if(findByEmail.isPresent()) {
			acRepository.delete(findByEmail.get());
		}
		EmailAuthData authData = EmailAuthData.builder().email(authCodePrefix + toEmail)
											.authCode(authCode)
											.expirationMills(authCodeExpirationMillis)
											.build();
		acRepository.save(authData);
		return true;
	}

	// 인증코드 생성
	private String createCode() {
		int length = 6;
		try {
			Random random = SecureRandom.getInstanceStrong();
			StringBuilder builder = new StringBuilder();
			for(int i=0; i<length; i++) {
				builder.append(random.nextInt(10));
			}
			return builder.toString();
		}catch(Exception e) {
			System.out.println("인증번호 생성 예외발생 : " + e);
			return null;
		}
	}
	
	// 인증코드 확인
	public boolean verificateCode(String email, String authCode, String service) {
		if(service.equals("signUp")) {
			authCodePrefix = "signUp ";
		}else if(service.equals("recover")) {
			authCodePrefix = "recover ";
		}else {
			return false;
		}
		
		Optional<EmailAuthData> authData = acRepository.findByEmail(authCodePrefix + email);
		if(authData.isPresent()) {
			// 만료시간 확인
			LocalDateTime regDate = authData.get().getRegDate();
			long expirationMills = authData.get().getExpirationMills();
			boolean isExpired = expirationCheck(regDate, expirationMills);
			
			// 인증코드 확인
			String savedAuthCode = authData.get().getAuthCode();
			
			if(!isExpired && savedAuthCode.equals(authCode)) {
				// 인증 완료
				acRepository.delete(authData.get());
				return true;
			}
		}
		
		return false;
	}
	
	public boolean expirationCheck(LocalDateTime regDate, long expirationMills) {
		boolean result = false;
		Duration duration = Duration.ofMillis(expirationMills);
		LocalDateTime expirationTime = regDate.plus(duration);
		if(LocalDateTime.now().isAfter(expirationTime)) {
			result = true;
		}
		return result;
	}
}
