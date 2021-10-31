package com.cos.blog.config.auth;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.blog.model.User;

import lombok.Getter;

// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면
// UserDetails 타입의 오브젝트를 스프링 시큐리티의 고유한 세션저장소에 저장함
@Getter
public class PrincipalDetail implements UserDetails{
	private User user; // 콤포지션(객체를 품고 있다)
	
	public PrincipalDetail(User user) {
		this.user = user;
	}
	
	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}

	// 계정이 만료되었는지 판단(true인 경우: 만료안됨)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정이 잠겨있는지 아닌지 리턴함(true인 경우: 잠기지 않음)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호가 만료되었는지 판단(true인 경우: 만료안됨)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	// 계정이 활성화되었는지 판단(true인 경우: 활성화)
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// 계정이 갖고있는 권한 목록을 리턴 (권한이 여러개 있을 수 있을 경우엔 루프를 돌아야함 현재: 1개)
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collectors = new ArrayList<>();
		collectors.add(()->{ 
			return "ROLE_"+user.getRole();
		});
		return collectors;
	}
}
