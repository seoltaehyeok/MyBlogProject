package com.cos.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;



	
//bean 등록: 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration // 빈 등록 (IoC)
@EnableWebSecurity // 시큐리티 필터가 등록이 된다.
@EnableGlobalMethodSecurity(prePostEnabled= true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크

public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean // IoC가 된다. => return 값을 spring이 관리함
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // csrf 토큰 비활성화 (테스트시 걸어두는 용도)
			.authorizeRequests() // request가 들어오면
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**") // 다음의 주소들은
				.permitAll()		// 모두 허가
				.anyRequest() // 다른 모든 요청은
				.authenticated() // 인증이 필요함
			.and()
				.formLogin()
				.loginPage("/auth/loginForm"); // 인증이 필요할 경우 로그인 페이지로 이동
	}
}
