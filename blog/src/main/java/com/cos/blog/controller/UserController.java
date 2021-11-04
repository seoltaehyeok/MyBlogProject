package com.cos.blog.controller;


import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

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
		
		@GetMapping("auth/kakao/callback")
		public @ResponseBody String kakaoCallback(String code) { // Data를 리턴해주는 컨트롤러 함수
			
			// POST 방식으로 key=value 데이터를 요청 (카카오에게)
			RestTemplate rt = new RestTemplate();
			
			// HttpHeader 오브젝트생성
			HttpHeaders headers = new HttpHeaders();
			headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8"); // 헤더에게 데이터타입이 key=value형태임을 알려줌
			
			
			// HttpBody 오브젝트 생성
			MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
			params.add("grant_type", "authorization_code");
			params.add("client_id", "fc74244df9199821608aa59f9bfe298d");
			params.add("redirect_uri", "http://localhost:8000/auth/kakao/callback");
			params.add("code", code);
			
			// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
			HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = 
					new HttpEntity<>(params, headers);
			
			
			// Http 요청하기 - Post 방식 - response 변수의 응답 받기
			ResponseEntity<String> response = rt.exchange(
					"https://kauth.kakao.com/oauth/token", // 토큰발급요청주소
					HttpMethod.POST, // 요청방식
					kakaoTokenRequest, // Body와 Header값
					String.class // 응답 받는 타입
					);
			
			return "카카오 토큰 요청완료: 토큰요청 응답: "+ response;
		}
}
