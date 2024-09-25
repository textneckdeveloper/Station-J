package com.green.security.handler;

import java.io.IOException;
import java.io.PrintWriter;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.nimbusds.jose.shaded.gson.JsonObject;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class LoginFailureHandler implements AuthenticationFailureHandler{

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		
		JsonObject json = new JsonObject();
		json.addProperty("message", "아이디 혹은 비밀번호가 일치하지 않습니다.");
		json.addProperty("redirectUrl", "/login");
		json.addProperty("success", false);
		
		PrintWriter out = response.getWriter();
		out.print(json.toString());
		out.flush();
	}

}
