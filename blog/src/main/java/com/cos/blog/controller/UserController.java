package com.cos.blog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

// 인증을 안해도 되는 경로인 경우 출입할 수 있게 경로를 /auth/... 허용
// 그냥 주소가 /(슬러쉬) 이면 index.jsp 허용 
// static이하에 있는 /js/**, /css/**, /image/** 허용
@Controller
public class UserController {
	
		@GetMapping("/auth/joinForm")
		public String joinForm() {
			return"user/joinForm"; 
		}
		
		@GetMapping("/auth/loginForm")
		public String loginForm() {
			return"user/loginForm"; 
		}
		
		@GetMapping("/user/updateForm")
		public String updateForm() {
			return"user/updateForm"; 
		}
}
