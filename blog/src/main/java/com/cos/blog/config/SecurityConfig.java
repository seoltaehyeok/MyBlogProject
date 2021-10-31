package com.cos.blog.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.blog.config.auth.PrincipalDetailService;


//bean 등록: 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
@Configuration // 빈 등록 (IoC)
@EnableWebSecurity // 시큐리티 필터가 등록이 된다.
@EnableGlobalMethodSecurity(prePostEnabled= true) // 특정 주소로 접근을 하면 권한 및 인증을 미리 체크

public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private PrincipalDetailService principalDetailService;
	
	@Bean // IoC가 된다. => return 값을 spring이 관리함
	public BCryptPasswordEncoder encodePWD() {
		return new BCryptPasswordEncoder();
	}
	
	// 시큐리티가 대신 로그인-> password가 가로채기
	// 해당 password가 무엇으로 해쉬가 되어 회원가입이 되었는지 알아야함
	// 같은 해쉬로 암호화해서 DB에 있는 해쉬랑 비교할 수 있음
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(principalDetailService).passwordEncoder(encodePWD());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable() // csrf 토큰 비활성화 (테스트시 걸어두는 용도)
			.authorizeRequests() // request가 들어오면
				.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**", "/dummy/**") // 다음의 주소들은
				.permitAll()		// 모두 허가
				.anyRequest() // 다른 모든 요청은
				.authenticated() // 인증이 필요함
			.and()
				.formLogin()
				.loginPage("/auth/loginForm") // 인증이 필요할 경우 로그인 페이지로 이동
				.loginProcessingUrl("/auth/loginProc") // Spring security가 해당 주소로 로그인을 가로챈 후 대신 로그인 해준다.
				.defaultSuccessUrl("/"); // 로그인이 끝나고(정상적으로 요청이 종료될 때) 이동하는 주소
	}
}
