package com.cos.blog.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

// @Controller //  주로 View를 반환하기 위해 사용
@ControllerAdvice // 모든 Exception이 이 Class로 들어옴
@RestController // Data를 return 해주는 Controller
public class GlobalExceptionHandler {
	
	@ExceptionHandler(value= Exception.class) // Exception 발생하면 에러를 이쪽으로 전달
	public String handleArgumentException(Exception e) {
		return "<h1>" + e.getMessage()+"</h1>";
	}
}
