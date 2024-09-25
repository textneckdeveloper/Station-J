package com.green.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.green.security.dto.AuthMemberDetails;
import com.green.service.MemberService;
import com.nimbusds.jose.shaded.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginSuccessHandler implements AuthenticationSuccessHandler{

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private MemberService service;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		// 인증객체에서 소셜로그인한 멤버 가져와서 확인하기
		AuthMemberDetails authMember = (AuthMemberDetails)authentication.getPrincipal();
		
		// 닉네임이 비어있으면 추가입력 페이지로 이동
		if(authMember.getNickName() == null) {
			redirectStrategy.sendRedirect(request, response, "http://localhost:3000/info/editinfo");
		}
		else {
			// 일반로그인시 아이디 저장하기 기능
			if(!authMember.isFromSocial()) {
				if(request.getParameter("rememberUsername") != null) {
					String remember = request.getParameter("rememberUsername");
					String rememberedUsername = request.getParameter("username");
					Cookie usernameCookie = new Cookie("rememberedUsername", rememberedUsername);
					int cookieMaxAge = 60*3; // 5분 설정(변경가능)
					if(remember.equals("true")) {
						usernameCookie.setMaxAge(cookieMaxAge);
						System.out.println(rememberedUsername);
					}else {
						usernameCookie.setMaxAge(0);
					}
					response.addCookie(usernameCookie);
				}
			}

			// 메인 페이지로 이동 (ADMIN 권한이 있으면 어드민 페이지로 이동)
			String targetUrl = "/";

			for(GrantedAuthority authority : authMember.getAuthorities()) {
				if(authority.getAuthority().equals("ROLE_ADMIN")){
					targetUrl = "/admin";
				}
			}
			
			if(authMember.isFromSocial()) {
				response.sendRedirect("http://localhost:3000/");
			}else {
				response.setContentType("application/json");
				response.setCharacterEncoding("UTF-8");
				
				JsonObject json = new JsonObject();
				json.addProperty("redirectUrl", targetUrl);
				json.addProperty("success", true);
				
				PrintWriter out = response.getWriter();
				out.print(json.toString());
				out.flush();
			}
		}
	}
}
