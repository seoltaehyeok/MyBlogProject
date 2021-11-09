package com.cos.blog.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.Board;
import com.cos.blog.repository.BoardRepository;

@RestController
public class ReplyControllerTest {
	
	@Autowired
	private BoardRepository boardRepository;
	
	@GetMapping("/test/board/{id}")
	public Board getBoards(@PathVariable int id) {
		return boardRepository.findById(id).get(); // jackson의 라이브러리(오브젝트를 json으로 리턴)=>	모델의 getter를 호출
	}
}
