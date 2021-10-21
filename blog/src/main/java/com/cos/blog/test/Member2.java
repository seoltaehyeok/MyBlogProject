package com.cos.blog.test;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
public class Member2 {
	//조건(메소드호출)을 통해 값을 바꾸기 위해서 필드 값은 대체로 private로 지정한다.
	private int id;
	private String username;
	private String password;
	private String email;
		
		public Member2(int id, String username, String password, String email) {
			this.id = id;
			this.username = username;
			this.password = password;
			this.email = email;
		}
}
