package com.gonnichiwa.controller.v1;

import com.gonnichiwa.config.secirity.JwtTokenProvider;
import com.gonnichiwa.entity.User;
import com.gonnichiwa.exception.CEmailSigninFailedException;
import com.gonnichiwa.model.response.CommonResult;
import com.gonnichiwa.model.response.SingleResult;
import com.gonnichiwa.repo.UserJpaRepo;
import com.gonnichiwa.service.ResponseService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Api(tags = {"1. Sign"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
@Slf4j
public class SignController {

	private final UserJpaRepo userJpaRepo;
	private final JwtTokenProvider jwtTokenProvider;
	private final ResponseService responseService;
	private final PasswordEncoder passwordEncoder;

	@ApiOperation(value = "로그인", notes = "이메일 회원 로그인을 한다")
	@GetMapping(value = "/signin")
	public SingleResult<String> signIn(@ApiParam(value = "회원 ID : 이메일", required = true) @RequestParam String email,
									   @ApiParam(value = "비밀번호", required = true) @RequestParam String password) {
		User user = userJpaRepo.findByUid(email).orElseThrow(CEmailSigninFailedException::new);
		if(!passwordEncoder.matches(password, user.getPassword())){
			log.debug(String.format("requested password(normal): %s", password));
			log.debug(String.format("requested password(encoded): %s", passwordEncoder.encode(password)));
			log.debug(String.format("requested password(from db, encoded): %s", user.getPassword()));
			throw new CEmailSigninFailedException("password incorrect");
		}
		return responseService.getSingleResult(jwtTokenProvider.createJwtToken(String.valueOf(user.getMsrl()), user.getRoles()));
	}

	@ApiOperation(value = "가입", notes = "회원 가입을 한다")
	@GetMapping(value = "/signup")
	public CommonResult signUp(@ApiParam(value = "회원 ID : 이메일", required = true) @RequestParam String email,
							   @ApiParam(value = "비밀번호", required = true) @RequestParam String password,
							   @ApiParam(value = "이름", required = true) @RequestParam String name) {
		userJpaRepo.save(
				User.builder()
					.uid(email)
					.password(passwordEncoder.encode(password))
					.name(name)
					.roles(Collections.singletonList("ROLE_USER"))
					.build()
		);
		return responseService.getSuccessResult();
	}
}
