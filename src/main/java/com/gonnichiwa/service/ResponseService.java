package com.gonnichiwa.service;

import com.gonnichiwa.model.response.CommonResult;
import com.gonnichiwa.model.response.ListResult;
import com.gonnichiwa.model.response.SingleResult;

import org.springframework.stereotype.Service;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Service
public class ResponseService {

	@AllArgsConstructor
	@Getter
	public enum CommonResponse {
		SUCCESS(0, "성공하였습니다!"),
		FAIL(1, "실패하였습니다!");

		int code;
		String msg;
	}

	// 단일건 결과를 처리하는 메소드
	public <T> SingleResult<T> getSingleResult(T data){
		SingleResult<T> result = new SingleResult<>();
		result.setData(data);
		setSuccessResult(result);
		return result;
	}
	// 다중건 결과를 처리하는 메소드
	public <T> ListResult<T> getListResult(List<T> list){
		ListResult<T> result = new ListResult<>();
		result.setList(list);
		setSuccessResult(result);
		return result;
	}
	// 성공 결과만 처리하는 메소드
	public CommonResult getSuccessResult(){
		CommonResult result = new CommonResult();
		setSuccessResult(result);
		return result;
	}
	// 실패 결과만 처리하는 메소드
	public CommonResult getFailResult(){
		CommonResult result = new CommonResult();
		result.setSuccess(false);
		result.setCode(CommonResponse.FAIL.getCode());
		result.setMsg(CommonResponse.FAIL.getMsg());
		return result;
	}
	// 실패 결과 code와 msg로 처리하는 메소드
	public CommonResult getFailResult(int code, String msg){
		CommonResult result = new CommonResult();
		result.setSuccess(false);
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}

	// 결과 모델에 api 요청 성공 데이터를 셋팅 해주는 메소드.
	private void setSuccessResult(CommonResult result) {
		result.setSuccess(true);
		result.setCode(CommonResponse.SUCCESS.getCode());
		result.setMsg(CommonResponse.SUCCESS.getMsg());
	}
}
