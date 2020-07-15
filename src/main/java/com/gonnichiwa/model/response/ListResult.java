package com.gonnichiwa.model.response;

import java.util.List;

import lombok.Getter;
import lombok.Setter;


/**
 * 결과가 여러 건인 api를 담는 모델
 * */
@Getter
@Setter
public class ListResult<T> extends CommonResult {
	private List<T> list;
}
