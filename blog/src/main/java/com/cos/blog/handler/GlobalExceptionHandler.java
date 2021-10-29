package com.cos.blog.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;

// @Controller //  주로 View를 반환하기 위해 사용
@ControllerAdvice // 모든 Exception이 이 Class로 들어옴
@RestController // Data를 return 해주는 Controller
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value= Exception.class) // Exception 발생하면 에러를 이쪽으로 전달
	public ResponseDto<String> handleArgumentException(Exception e) {
		return new ResponseDto<String>(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()); // 자바오브젝트를 JSON으로 변환해서 리턴(Jackson)
	}
}
