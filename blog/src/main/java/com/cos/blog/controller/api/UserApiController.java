package com.cos.blog.controller.api;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.dto.ResponseDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	
	@Autowired
	private AuthenticationManager authenticationManger;
	
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) { // username, password, email
		System.out.println("UserApiController: save호출됨");
		userService.회원가입(user); // 성공시 1, 실패시 -1 
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); // 자바오브젝트를 JSON으로 변환해서 리턴(Jackson)
	}
	
	@PutMapping("/user") 
	public ResponseDto<Integer> update(@RequestBody User user) { // RequestBody를 걸어주어야 JSON데이터를 받을 수 있음
		userService.회원수정(user);
		// 여기서는 트랜잭션이 종료되기 때문에 DB값이 변경이 됨
		// 하지만 세션값은 변경되지 않은 상태이기 때문에 직접변경이 필요
		// 세션등록
		Authentication authentication = authenticationManger.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
}
	
	// 전통적인 로그인 방식
//	@PostMapping("/api/user/login")
//	public ResponseDto<Integer> login(@RequestBody User user, HttpSession session) {
//		System.out.println("UserApiController: login호출됨");
//		User principal = userService.로그인(user); // principal : 접근주체
//		
//		if(principal != null) {
//			session.setAttribute("principal", principal); // 세션 생성
//		}
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); // 자바오브젝트를 JSON으로 변환해서 리턴(Jackson)
//	}

