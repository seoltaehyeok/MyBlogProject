package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// html이 아닌 data를 리턴해주는 controller = RestController
@RestController 
public class DummyControllerTest {
	
	@Autowired // 의존성 주입
	private UserRepository userRepository;
	

	@DeleteMapping("/dummy/user/{id}") //Delete Test
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		} catch (EmptyResultDataAccessException e) {
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		return "삭제되었습니다. id: " +id;
	}
	
	// save함수는 id를 전달하지 않으면 insert를 해줌,
	// save함수는 id를 전달하면 해당 id에 대한 데이터가 있을 경우 update를 해줌
	// save함수는 id를 전달하면 해당 id에 대한 데이터가 없을 경우 insert를 해줌
	// email, password
	@Transactional // DB에서 객체를 들고와서 객체만 변경하고 save()를 호출하지 않았을 경우(저장하지 않았을 경우)에도 저장을 해줌 => 더티체킹 (함수종료시 자동commit)
	@PutMapping("dummy/user/{id}") // Update Test
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { // Json데이터요청=>java Object(MessageConverter의 Jackson라이브러리가 변환해서 받아줌)
		System.out.println("id : "+ id);
		System.out.println("password: "+ requestUser.getPassword());
		System.out.println("email: "+ requestUser.getEmail());
		
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		
		// @ 더티체킹: 영속화가 된 상태로 오브젝트의 값이 변경이 됐으므로 Transactional이 변경을 감지하여 DB에 Update를 하게끔 알려줌
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		// userRepository.save(user);
		
		return user;
	}
	
	// http://localhost:8000/blog/dummy/user
	@GetMapping("/dummy/users")
	public List<User> list() {
		return userRepository.findAll();
	}
	
	// 한페이지당 2건의 데이터를 리턴받기
	@GetMapping("/dummy/user")
	// 페이지 2건, 종류=id, id는 최신순
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingusers = userRepository.findAll(pageable);
		
		List<User> users = pagingusers.getContent();
		return users;
	}
	
	// {id} 주소로 파라미터를 전달 받을 수 있음
	// http://localhost:8000/blog/dummy/user/3(id값)
	@GetMapping("/dummy/user/{id}")
	public  User detail(@PathVariable int id) {
		// user/4 를 찾으면 내가 데이터베이스에서 못찾아오게 된다면 user가 null이 됨 => return null이 되고, 프로그램에 문제가 생김
		// => Optional로 User 객체를 감싸서 가져오고, null인지 아닌지 판단해서 return함
		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id : "+id);
			}
		});
		// 요청 : 웹브라우저
		// user 객체 = 자바 오브젝트 (웹브라우저는 java object를 인식할 수 없음)
		// 변환 (웹브라우저가 이해할 수 있는 데이터로) -> json으로 변환
		// 스프링부트 = MessageConverter라는 애가 응답시에 자동 작동
		// 만약 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출
		// user 오브젝트를 json으로 변환해서 브라우저에게 던져준다 => 웹페이지에서 json타입의 데이터로 보여짐
		return user;
	}
	
	// http://localhost:8000/blog/dummy/join (요청)
	// http의 body에 username, password, email 데이터를 가지고 (요청)
	@PostMapping("/dummy/join") // Create Test
	public String join(User user) { // key=value (약속된 규칙)
		System.out.println("username: " + user.getUsername() );
		System.out.println("password: " + user.getPassword() );
		System.out.println("email: " + user.getEmail() );
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}
