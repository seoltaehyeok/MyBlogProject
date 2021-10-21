package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempControllerText {
	
	// http://localhost:8000/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		System.out.println("tempeHome");
		// 파일리턴 기본경로: src/main/resources/static
		//안되는이유: src/main/resources/statichome.html
		//리턴명: /home.html로 해야 가능
		//src/main/resources/static/home.html
		return "/home.html";
	}

		@GetMapping("temp/jsp")
		public String tempJsp() {
			// prefix : /WEB-INF/views/
			// suffix : .jsp
			// 풀네임 : /WEB-INF/views/test.jsp 로 찾게 됨
			return "test";
		}
}

