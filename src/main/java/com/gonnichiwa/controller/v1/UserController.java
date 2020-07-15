package com.gonnichiwa.controller.v1;

import com.gonnichiwa.entity.User;
import com.gonnichiwa.exception.CUserNotFoundException;
import com.gonnichiwa.model.response.CommonResult;
import com.gonnichiwa.model.response.ListResult;
import com.gonnichiwa.model.response.SingleResult;
import com.gonnichiwa.repo.UserJpaRepo;
import com.gonnichiwa.service.ResponseService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@Api(tags = {"2. User"})
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class UserController {

	private final UserJpaRepo userJpaRepo;
	private final ResponseService responseService; // 결과를 처리할 api

	@ApiOperation(value = "회원 조회", notes = "모든 회원을 조회한다")
	@ApiImplicitParams(
			{@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")}
	)
	@GetMapping(value = "/user")
	public ListResult<User> findAllUser() {
		// 결과 데이터가 여러간인 경우 getListResult를 이용해서 결과를 출력한다.
		return responseService.getListResult(userJpaRepo.findAll());
	}

	@ApiOperation(value = "회원 입력", notes = "회원을 입력한다.")
	@ApiImplicitParams(
			{@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")}
	)
	@PostMapping(value = "/user")
	public SingleResult<User> save(@ApiParam(value = "회원 아이디", required = true) @RequestParam String uid,
								   @ApiParam(value = "회원 이름", required = true) @RequestParam String name) {
		User user = User.builder()
				.uid(uid)
				.name(name)
				.build();
		return responseService.getSingleResult(userJpaRepo.save(user));
	}

	@ApiOperation(value = "회원 수정", notes = "회원번호(msrl)로 회원 정보를 수정한다")
	@ApiImplicitParams(
			{@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")}
	)
	@PutMapping(value = "/user")
	public SingleResult<User> modify(
			@ApiParam(value = "회원번호", required = true) @RequestParam long	msrl,
			@ApiParam(value = "회원이름", required = true) @RequestParam String name) {
		User user = User.builder()
				.msrl(msrl)
				.name(name)
				.build();
		return responseService.getSingleResult(userJpaRepo.save(user));
	}

	@ApiOperation(value = "회원 단건 조회", notes = "회원번호(msrl)로 회원을 조회한다")
	@ApiImplicitParams(
			{@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")}
	)
	@GetMapping(value = "/user/{msrl}")
	public SingleResult<User> findUserByUserId(@ApiParam(value = "회원ID", required = true) @PathVariable long msrl) {
		// SecurityContext에서 인증받은 회원의 정보를 얻어온다.
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String id = authentication.getName();
		return responseService.getSingleResult(userJpaRepo.findByUid(id).orElseThrow(CUserNotFoundException::new)); // 커스텀 익셉션 발생 시킬 때
	}

	@ApiOperation(value = "회원 삭제", notes = "회원번호(msrl)로 회원 정보를 삭제한다")
	@ApiImplicitParams(
			{@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")}
	)
	@DeleteMapping(value = "/user/{msrl}")
	public CommonResult delete(@ApiParam(value = "회원 번호", required = true) @PathVariable long msrl) {
		userJpaRepo.deleteById(msrl);
		// 성공 결과 정보만 필요한 경우 getSuccessResult()를 이용하여 결과를 출력한다.
		return responseService.getSuccessResult();
	}

}