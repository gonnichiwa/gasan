package com.gonnichiwa.config.secirity;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtTokenProvider { // JWT 토큰을 생성 및 검증 모듈

	@Value("spring.jwt.secret") // resource/application.yml의 value 가져옴.
	private String secretKey;

	private long tokenValidMillisecond = 1000L * 60 * 60; // 1 hour

	private final UserDetailsService userDetailsService;

	@PostConstruct
	protected void init(){
		secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
	}

	// Jwt 토큰 생성
	public String createJwtToken(String userPk, List<String> roles){
		Claims claims = Jwts.claims().setSubject(userPk);
		claims.put("roles", roles);
		Date now = new Date();
		return Jwts.builder()
				.setClaims(claims) // 데이터
				.setIssuedAt(now)  // 토큰 발행일자
				.setExpiration(new Date(now.getTime() + tokenValidMillisecond)) // set Expire Time
				.signWith(SignatureAlgorithm.HS256, secretKey)				   // 암호화 알고리즘, secret값에 암호화 적용.
				.compact();
	}

	// Jwt 토큰으로 인증 정보를 조회
	public Authentication getAuthentication(String token){
		UserDetails userDetails = userDetailsService.loadUserByUsername(this.getUserPk(token));
		return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
	}

	// Jwt token에서 회원 구별 정보 get
	public String getUserPk(String token){
		return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody().getSubject();
	}

	// Request의 Header에서 token 파싱 : "X-AUTH-TOKEN: jwt 토큰"
	public String resolveToken(HttpServletRequest request){
		return request.getHeader("X-AUTH-TOKEN");
	}

	// Jwt 토큰의 유효성 + 만료일자 확인
	public boolean isValidateToken(String jwtToken){
		try {
			Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
			return !claims.getBody().getExpiration().before(new Date());
		} catch (Exception e){
			log.debug(e.getMessage());
			return false;
		}
	}


}
