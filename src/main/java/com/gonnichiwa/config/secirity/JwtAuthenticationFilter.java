package com.gonnichiwa.config.secirity;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

public class JwtAuthenticationFilter extends GenericFilterBean {

	private JwtTokenProvider jwtTokenProvider;

	// DI JwtTokenProvider
	public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider){
		this.jwtTokenProvider = jwtTokenProvider;
	}

	// 역할 : Request로 들어오는 Jwt Token의 유효성을 검증(jwtTokenProvider.validateToken) 하는 filter
	//       이 fileter(JwtAuthenticationFilter) 를 spring boot FilterChain에 등록.
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
		String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
		if(token != null && jwtTokenProvider.isValidateToken(token)){
			Authentication auth = jwtTokenProvider.getAuthentication(token);
			SecurityContextHolder.getContext().setAuthentication(auth);
		}
		filterChain.doFilter(request, response);
	}
}
