package com.green.security.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.green.security.handler.LoginFailureHandler;
import com.green.security.handler.LoginSuccessHandler;

import jakarta.servlet.DispatcherType;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		// 비밀번호 평문을 저장하고 사용가능하게 하는 커스텀 패스워드 인코더 
		String idForEncode = "plaintext";
	    Map<String, PasswordEncoder> encoders = new HashMap<>();
	    encoders.put(idForEncode, new PlainTextPasswordEncoder());
	    DelegatingPasswordEncoder delegatingPasswordEncoder = new DelegatingPasswordEncoder(idForEncode, encoders);
	    //return delegatingPasswordEncoder;
	    return new BCryptPasswordEncoder();
	}
	

	
	// 시큐리티 필터체인 설정
	@Bean
	public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		http.exceptionHandling(e->{
			e.authenticationEntryPoint((request, response, authException)->{
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "권한이 없습니다.");
			});
		});
		
		// 링크별 권한
		http.authorizeHttpRequests((auth)->{
			auth.dispatcherTypeMatchers(DispatcherType.FORWARD).permitAll()
			.requestMatchers("/access-denied").permitAll()
			.requestMatchers("/info/**").authenticated()
			.requestMatchers("/asset/**").permitAll()
			.requestMatchers("/archive/write/**", "/archive/modify/**", "/archive/remove/**").hasAnyRole("STAFF", "SUBADMIN", "ADMIN")
			.requestMatchers("/video/write/**", "/video/modify/**", "/video/remove/**").hasAnyRole("STAFF", "SUBADMIN", "ADMIN")
			.requestMatchers("/admin/getSiteInfo").hasAnyRole("STAFF", "SUBADMIN", "ADMIN")
			.requestMatchers("/admin/**").hasRole("ADMIN")
			.anyRequest().permitAll();
		});
		// 권한 없음 페이지
		http.exceptionHandling(e->{
			e.accessDeniedHandler(accessDeniedHandler());
		});
		
		http.formLogin((form)->{
			// 기본 로그인 페이지 설정
			form.loginPage("/signin")
			//.loginProcessingUrl("/signin")
			// 로그인 이후 기본 연결주소 두번째 매개값 true일 경우 항상 이 주소로
			.defaultSuccessUrl("/", true)
			.successHandler(loginSuccessHandler())
			.failureHandler(new LoginFailureHandler())
			.permitAll();
		});
		// 소셜 로그인
		http.oauth2Login(oauth->{
			// 소셜 로그인 성공시 추가정보 입력여부를 위한 핸들러
			oauth.successHandler(loginSuccessHandler());
		});
		// 로그아웃
		http.logout((form)->{
			form.invalidateHttpSession(true);
			form.deleteCookies("JSESSIONID");
	        form.permitAll();
		});
		http.cors(cors->
			cors.configurationSource(corsConfigurationSource()));

		return http.build();
	}
	
	@Bean
	public LoginSuccessHandler loginSuccessHandler() {
		return new LoginSuccessHandler();
	}
	
	@Bean
	public CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration config = new CorsConfiguration();
		
		config.addAllowedOriginPattern("*");
		config.addAllowedHeader("*");
		config.addAllowedMethod("*");
		config.setAllowCredentials(true);
		
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", config);
		return source;
	}
	
	private AccessDeniedHandler accessDeniedHandler() {
        return (request, response, accessDeniedException) -> {
            if (isAuthenticated()) {
                response.sendRedirect("/access-denied");
            } else {
                response.sendRedirect("/login");
            }
        };
	}
	
	private boolean isAuthenticated() {
        return SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication() 
                  instanceof AnonymousAuthenticationToken);
	}
}
