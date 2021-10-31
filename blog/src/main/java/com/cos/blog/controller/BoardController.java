package com.cos.blog.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BoardController {
	
	@GetMapping({"", "/"})
	public String index() {
		//prefix: /WEB-INF/views/
		//suffix: .jsp
		//System.out.println("로그인 사용자 아이디: "+principal.getUsername());
		return "index"; //WEB-INF/views/index.jsp
	}
	
	// USER권한 필요
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "/board/saveForm";
	}
}
