package com.cos.blog.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

// 스프링 com.cos.blog 패키지 이하를 스캔해서 모든 파일을 메모리에 new하지x
// 특정 어노테이션이 붙어있는 클래스 파일들을 new해서(IoC) 스프링 컨테이너를 관리해줍니다.
@RestController 
public class BlogControllerTest {
	
	//http://localhost:8000/test/hello
	@GetMapping("/test/hello")
	public String hello() {
		return "<h1>hello Spring boot</h1>";
	}
}
