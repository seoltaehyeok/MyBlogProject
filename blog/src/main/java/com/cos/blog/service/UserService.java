package com.cos.blog.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

//스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 함 => IoC를 함
@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder encoder;
	
	@Transactional(readOnly = true)
	public User 회원찾기(String username) {
		User user = userRepository.findByUsername(username).orElseGet(()->{
			return new User(); // 예외 상태 시 빈 객체 넣어주기
		});
		return user;
	}
	
	@Transactional // 하나의 로직이 됨 (전체가 성공하면 commit, 실패시 롤백)
	public void 회원가입(User user) {
		String rawPassword = user.getPassword(); // 1234원문
		String encPassword = encoder.encode(rawPassword); // 해쉬
		user.setPassword(encPassword); // 해쉬한 값을 비밀번호에 넣어줌
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
	

	
	@Transactional
	public void 회원수정(User user) {
		// 수정시 영속성 컨텍스트 User 오브젝트를 영속화, 영속화된 User 오브젝트를 수정하는 방식
		// select를 해서 User 오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해
		// 영속화된 오브젝트를 변경하면 자동으로 DB 업데이트가 됨
		User persistance = userRepository.findById(user.getId())
				.orElseThrow(()->{
			return new IllegalArgumentException("회원찾기 실패");
		});
		
		// Validate 체크 => oauth에 값이 없으면 수정 가능
		if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
			String rawPassword = user.getPassword(); // 변경된 비밀번호를 rawPassword에 넣어줌
			String encPassword = encoder.encode(rawPassword); // rawPassword를 암호화된 비밀번호로 변경
			persistance.setPassword(encPassword); 
			persistance.setEmail(user.getEmail());
			// 회원수정 함수 종료 시 = 서비스 종료 = 트랜잭션 종료 => 커밋 활성화
			// 영속화된 persistance 객체읭 변화가 감지되면 더티체킹되어 update문을 날려준다.
		}
	}
}


//	@Transactional(readOnly = true) // Select할 때 트랜잭션 시작, 서비스 종료시에 트랜잭션 종료 (정합성 유지)
//	public User 로그인(User user) {
//			return userRepository.findByUsernameAndPassword(user.getUsername(), user.getPassword());
//	}

