package com.gonnichiwa.exception;

import com.gonnichiwa.model.response.CommonResult;
import com.gonnichiwa.service.ResponseService;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

/**
 * Exception이 발생했을 때 공통처리해줌.
 * */
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionAdvice {

	private final ResponseService responseService;

	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult defaultException(HttpServletRequest request, Exception e) {
		return responseService.getFailResult();
	}

	/**
	 * Custom Exception 예시
	 * */
	@ExceptionHandler(CUserNotFoundException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult userNotFoundException(HttpServletRequest request, CUserNotFoundException e) {
		return responseService.getFailResult();
	}

	@ExceptionHandler(CEmailSigninFailedException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	protected CommonResult emailSigninFailed(HttpServletRequest request, CEmailSigninFailedException e){
		return responseService.getFailResult(-1001, "Your account does not exist or your email or password is incorrect");
	}

}
