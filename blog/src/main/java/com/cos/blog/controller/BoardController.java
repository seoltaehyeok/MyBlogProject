package com.cos.blog.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;


import com.cos.blog.service.BoardService;


@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	// 컨트롤러에서 세션을 어떻게 찾나요?
	// @AuthenticationPrincipal PrincipalDetail pricipal
	@GetMapping({"", "/"})
	public String index(Model model,@PageableDefault(size=3, sort="id", direction = Sort.Direction.DESC) Pageable pageable) {
		model.addAttribute("boards", boardService.글목록(pageable));
		//prefix: /WEB-INF/views/
		//suffix: .jsp
		//System.out.println("로그인 사용자 아이디: "+principal.getUsername());
		return "index"; //WEB-INF/views/index.jsp
	}
	
	// USER권한 필요
	@GetMapping("/board/saveForm")
	public String saveForm() {
		return "/board/saveForm";
	}
}
