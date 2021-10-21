package com.cos.blog.test;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// 사용자가 요청 -> 응답(HTML파일)
// @Controller

// 사용자가 요청 -> 응답(Data)
@RestController
public class HttpContorllerTest2 {
	
	private static final String TAG = "HttpControllerTest: ";
	@GetMapping("/http/lombok")
	public String lombokTest() {
		//빌더패턴 (넣고 싶은 객체만 사용가능)
		Member2 m2 = new Member2.Member2Builder().username("ssar").password("1234").email("ssar@nate.com").build();
		System.out.println(TAG +"getter: " +m2.getId());
		
		Member2 m = new Member2(1, "ssar", "1234", "email");
		System.out.println(TAG +"getter: " +m.getId());
		
		m.setId(5000);
		System.out.println(TAG +"getter: "+m.getId());
		return "lombok test완료";
	}
	//인터넷 브라우저 요청은 무조건 get요청만 가능
	//http://localhost:8000/http/get (select)
	@GetMapping("/http/get")
	public String getTest(Member2 m) { //id=1&username=ssar&password=1234&email=ssar@nate.com (get요청은 querryString 으로만 가능)
		return "get 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() +", " + m.getEmail();
	}
	
	//http://localhost:8000/http/post (insert)
	@PostMapping("/http/post")
	public String postTest(@RequestBody Member2 m) {
		return "post 요청 : " + m.getId() + ", " + m.getUsername() + ", " + m.getPassword() +", " + m.getEmail();
	}
	
	//http://localhost:8000/http/put (update)
	@PutMapping("/http/put")
	public String putTest(@RequestBody Member2 m) {
		return "put 요청 : "+ m.getId() +", "+ m.getPassword();
	}
	
	//http://localhost:8000/http/delete (delete)
	@DeleteMapping("/http/delete")
	public String deleteTest() {
		return "delete 요청";
	}
	
	
}
