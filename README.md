# MyBlogProject (21.11.09 구현 완)

## 구현 CRUD

계정생성 / 이메일, 비밀번호 변경기능 / 페이지(홈, 로그인, 회원가입) / 예외처리

## User
로그인 환경 제공

아이디(PK) / 패스워드 / 이메일 / 역할(ex. 관리자, 회원, 매니저) / 사용자명 / 로그인시간
## Reply
게시물에 대한 답변 게시판 환경 제공

아이디(PK) / 콘텐츠(게시물) / 게시물의 답글(FK) / 답변자명(FK) / 답글 작성시간
## Board
게시판&게시물 환경 제공

아이디(PK) / 콘텐츠(제목) / 콘텐츠(게시물) / 조회수 / 작성자명(FK) / 작성시간 / 게시물의 답글(Board를 select 할때 reply 필요)
## yml
    jpa:
        open-in-view: true
        hibernate:
          ddl-auto: create # (create, none, update) 최초: craate, 그 후 update
          naming:
            physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
          use-new-id-generator-mappings: false
        show-sql: true  # 콘솔창에 보이게끔 출력
        properties:
          hibernate:
            format_sql: true  # 가독성 좋게 들여쓰기 사용

create상태로 컴파일시 콘솔창: 

    Hibernate: 
    drop table if exists User
    
    Hibernate:
        create table User (
           id integer not null auto_increment,
            createDate datetime(6),
            email varchar(50) not null,
            password varchar(100) not null,
            role varchar(255) default 'user',
            username varchar(30) not null,
            primary key (id)
        ) engine=InnoDB

        기존의 테이블을 삭제 후 새로운 테이블을 생성함

## Workbench
table create

<img src="https://user-images.githubusercontent.com/83220871/136510865-1f58d224-59e3-4b62-b257-b2dff0fca0a7.png" width="200" height="150"/> <img src="https://user-images.githubusercontent.com/83220871/136516718-d641c0dc-b1b8-4f5e-9cd7-d8adbe8bea30.png" width="200" height="150"/> <img src="https://user-images.githubusercontent.com/83220871/136522001-48107d3e-b86c-4a3a-89bf-4ea6f0152376.png" width="200" height="150"/>

## Keypoint
username 즉, 아이디의 경우 중복되면 안되므로 unique=true를 통해 고유값 지정


## <인증>

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http
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

## 인증 불필요
	.antMatchers("/", "/auth/**", "/js/**", "/css/**", "/image/**") // 다음 주소를 갖는 경우

## 인증 필요
그 외 모든 경로의 주소

## 인증되지 않은 페이지 요청시 로그인 페이지로 이동

	.loginPage("/auth/loginForm") // 인증이 필요할 경우 로그인 페이지로 이동
	
<img src ="https://user-images.githubusercontent.com/83220871/139435473-340e56f4-13f8-48ff-ab00-4080b51b1094.png" width="300" height="200"/>

## 비밀번호 해쉬(암호화)
    @Transactional // 하나의 로직이 됨 (전체가 성공하면 commit, 실패시 롤백)
	public void 회원가입(User user) {
		String rawPassword = user.getPassword(); // 1234원문
		String encPassword = encoder.encode(rawPassword); // 해쉬
		user.setPassword(encPassword); // 해쉬한 값을 비밀번호에 넣어줌
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}

## CSRF토큰 사용
ID값을 Ajax를 통해 회원가입 요청 -> CSRF토큰이 없을 경우 Spring Security(서버)는 요청을 거절

사용자 세션의 요청에 대해 서버단에서 검증하는 방법

요청 시 CSRF토큰을 생성-> 유효 아이디에 고유값 생성 -> 세션에 저장

=> 요청할 때마다 CSRF토큰 서버쪽으로 전송 -> 정상이라고 판단하면 허용

	@Override
		protected void configure(HttpSecurity http) throws Exception {
			http
			    .csrf().disable() // csrf 토큰 비활성화 (테스트시 걸어두는 용도)
			    ...
	}
	
## PrincipalDetailService.java (스프링 시큐리티의 로그인 가로채기)
Spring Security가 요청한 username, password를 가로채게 하기위해 loginProc는 생성x

<details>
<summary>SecurityConfig.java - 가로채기 코드</summary>
<div markdown="1">     
	
    http
		...
	     .and()
		...
		.loginProcessingUrl("/auth/loginProc") // Spring security가 해당 주소로 로그인을 가로챈 후 대신 로그인 해준다.
		.defaultSuccessUrl("/"); // 로그인이 끝나고(정상적으로 요청이 종료될 때) 이동하는 주소
		....
	
</div>
</details>

	@Service // Bean 등록이 됨
	public class PrincipalDetailService implements UserDetailsService{

		// 스프링이 로그인 요청을 가로챌 때, username, password 변수 2개 가로챔
		// => password 부분 처리는 자동
		// username이 DB에 있는지 확인해서 return을 해주면 된다.
		@Autowired
		private UserRepository userRepository;

		@Override
		public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
			// userRepository -> Optional<User> findByUsername(String username);
			User principal = userRepository.findByUsername(username) 
					.orElseThrow(()->{
						return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : "+username);
					});
			return new PrincipalDetail(principal); // 시큐리티 세션에 유저정보 저장
		}
	}

userDetails타입 userObject생성-> 로그인 요청 및 세션 등록->  
<details>
<summary>PrincipalDetail.java</summary>
<div markdown="1">       

	// 스프링 시큐리티가 로그인 요청을 가로채서 로그인을 진행하고 완료가 되면
	// UserDetails 타입의 오브젝트를 스프링 시큐리티의 고유한 세션저장소에 저장함
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
</div>
</details>

## 글쓰는 User의 정보를 가져오기

BoardApiController.java에서 BoardService를 @Autowired로 DI를 함
@AuthenticationPrincipal PrincipalDetail principal를 통해 User 오브젝트를 가져온다.
BoardService의 글쓰기 메소드를 통해 매개변수 User값을 받고, Board에 user값을 넣어준다.

	@RestController
	public class BoardApiController {

		@Autowired
		private BoardService boardService;

		@PostMapping("/api/board")
		public ResponseDto<Integer> save(@RequestBody Board board, @AuthenticationPrincipal PrincipalDetail principal) { 
			boardService.글쓰기(board, principal.getUser());
			return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
		}
	}

<details>
<summary>PrincipalDetail.java</summary>
<div markdown="1"> 

	@Getter
	public class PrincipalDetail implements UserDetails{
		private User user; // 콤포지션(객체를 품고 있다)
	
	public PrincipalDetail(User user) {
		this.user = user;
	}
	...
	
	}

</div>
</details>

<details>
<summary>BoardService.java</summary>
<div markdown="1"> 

	@Transactional
		public void 글쓰기(Board board, User user) { // title, content 두가지의 값 받음
			board.setCount(0);
			board.setUser(user);
			boardRepository.save(board);
		}
</div>
</details>

## 글 목록보기
<details>
<summary>자료</summary>
<div markdown="1"> 
<image src ="https://user-images.githubusercontent.com/83220871/139571969-ba7f37e2-a17d-44b8-bf12-e8055eb2152c.gif" width="800" height="400"/>
</div>
</details>


BoardContorller에서 index를 불러올 때, boardService.글목록() 으로

BoardService.java에서 List<Board>글목록() 을 통해 모든 board 레퍼지토리의 모든값을 반환한다.


BoardContorller.java 

	@Controller
	public class BoardController {

		@Autowired
		private BoardService boardService;

		@GetMapping({"", "/"})
		public String index(Model model) {
			model.addAttribute("boards", boardService.글목록());
			
			//System.out.println("로그인 사용자 아이디: "+principal.getUsername());
			return "index";
		}


<details>
<summary>BoardService.java</summary>
<div markdown="1"> 

	public List<Board> 글목록() {
			return boardRepository.findAll();
		}
</div>
</details>


홈 화면(index.jsp)에서 갱신된 글 목록을 불러와야 하기 때문에 index.jsp에 동기화를 시켜주어야 한다.

header.jsp 파일의 jstl을 통해 index에서 jstl을 사용할 수 있다.

	<!-- header.jsp파일의 JSTL 설정--!>	
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

index에서 <c:forEach var="board" items="${boards}"> 를 통해

	<!-- 요청정보가 넘어올때 JSTL의 EL 표현식: boards를 받을 수 있음 -->
	<c:forEach var="board" items= "${boards}">
		<!-- profile card -->
		<div class="card m-2">
			<!--  <img class="card-img-top" src="img_avatar1.png" alt="Card image"> -->
			<div class="card-body">
				<h4 class="card-title">${board.title}</h4> <!-- board.getTitle() 이 호출되는것과 같은 원리 -->
				<a href="#" class="btn btn-primary">See Post</a>
			</div>
		</div>
	</c:forEach>

BoardController에 있는 return index가 반환될 때 

model.addAttribute("boards", boardService.글목록());의 boards도 함께 반환된다.

	@GetMapping({"", "/"})
	public String index(Model model) {
		model.addAttribute("boards", boardService.글목록());
		return "index";
	}

## 페이징 처리

<details>
<summary>index.jsp 페이징 구현</summary>
<div markdown="1"> 
<image src="https://user-images.githubusercontent.com/83220871/139574434-1e1f11e0-febd-4a0a-b2db-91ad4fbcc952.gif" width="800" height="400"/>
</div>
</details>

<details>
<summary>List<>방식</summary>
<div markdown="1"> 

	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingusers = userRepository.findAll(pageable);
		
		List<User> users = pagingusers.getContent();
		return users;
	}

<image src="https://user-images.githubusercontent.com/83220871/139573986-5a06490e-94a9-4694-be3f-9ae623e861cb.png" width="400" height="200"/>

</div>
</details>

<details>
<summary>Page<>방식</summary>
<div markdown="1"> 

	@GetMapping("/dummy/user")
	public Page<User> pageList(@PageableDefault(size=2, sort="id", direction = Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingusers = userRepository.findAll(pageable);
		
		List<User> users = pagingusers.getContent();
		return pagingusers;
	}

<image src="https://user-images.githubusercontent.com/83220871/139574047-4e52dbd8-6c14-42a3-b350-82de22005818.png" width="400" height="600"/>

</div>
</details>

Page<> 방식을 사용해야 각 페이지의 정보를 얻을 수 있다.

	public Page<Board> 글목록(Pageable pageable) {
		return boardRepository.findAll(pageable);
	}


<details>
<summary>index.jsp 페이지처리</summary>
<div markdown="1"> 

	<!-- 이전 페이지 -->
	<c:choose>
		<c:when test="${boards.first}">
			<li class="page-item disabled"><a class="page-link" href="?page=${boards.number-1 }">Previous</a></li>
		</c:when>
		<c:otherwise>
			<li class="page-item"><a class="page-link" href="?page=${boards.number-1 }">Previous</a></li>
		</c:otherwise>
	</c:choose>

	<!-- 페이지 번호 -->
	<c:forEach var="i" begin="1" end="${boards.totalPages}">
		<li class="page-item"><a class="page-link" href="?page=${i-1}">${i}</a></li>
	</c:forEach>

	<!--  다음 페이지 -->
	<c:choose>
		<c:when test="${boards.last}">
			<li class="page-item disabled"><a class="page-link" href="?page=${boards.number+1 }">Next</a></li>
		</c:when>
		<c:otherwise>
			<li class="page-item"><a class="page-link" href="?page=${boards.number+1 }">Next</a></li>
		</c:otherwise>
	</c:choose>

</div>
</details>

## 페이지 상세보기

<details>
<summary>상세보기 구현</summary>
<div markdown="1"> 
<image src="https://user-images.githubusercontent.com/83220871/139624071-9a0a3adb-9761-4078-9ed8-9b0fe8f4ad73.gif" width="800" height="400"/>
</div>
</details>



index(홈)에서 See Post 버튼을 누르면 /board/${board.id} 로 이동

	<a href="/board/${board.id}" class="btn btn-primary">See Post</a>

index에서 /board/${board.id} 요청이 들어올 때 BoardController.java의 GetMapping에 의해 아래의 메소드가 동작한다.

	@GetMapping("/board/{id}")
	public String findById(@PathVariable int id, Model model) {
		model.addAttribute("board", boardService.글상세보기(id));
		return "board/detail";
	}

리턴 값으로 board/detail을 board폴더 하위에 있는 detail.jsp가 반환된다.

	<div class="container">

			<button class="btn btn-secondary" onclick="history.back()">돌아가기</button>
			<button id="btn-update" class="btn btn-warning">수정</button>
			<button id="btn-delete" class="btn btn-danger">삭제</button>
			<br /><br />
			<div>
				<h3>${board.title}</h3>
			</div>
			<hr/>
			<div>
				<div>${board.content}</div>
			</div>
			<hr />
	</div>


## 글 삭제하기

<details>
<summary>게시글 삭제 구현</summary>
<div markdown="1"> 
<image src="https://user-images.githubusercontent.com/83220871/139627879-9e6c77cc-e6b6-4263-8cf6-d645b581e104.gif" width="800" height="400"/>
</div>
</details>

글을 삭제하기 위해서는 글을 작성한 사용자에 한에 글을 삭제할 수 있어야한다.

삭제버튼을 글을 작성한 사용자에 한에 표시한다.
board.user.id와 principal.user.id가 같은 경우에 버튼이 표시

	<c:if test="${board.user.id == principal.user.id}">
		<button id="btn-delete" class="btn btn-danger">삭제</button>
	</c:if>

삭제버튼을 누른경우 다음과 같이 동작하도록 한다.
	
	$("#btn-delete").on("click", () => {
				this.deleteById();
			});
	...

	deleteById: function() {
		var id = $("#id").text();  // 글 번호 : <span id="id"><i>${board.id} </i></span>의 ${board.id}가 text이므로 .text();
		
		$.ajax({
			type: "DELETE",
			url: "/api/board/"+id,
			dataType: "json" 
		}).done(function() {
			alert("삭제가 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) { // 실패일때
			alert(JSON.stringify(error));
		});
	}

url: "/api/board/"+id을 통해 DeleteMapping이 동작하고 아래의 메소드가 동작한다.

	@DeleteMapping("/api/board/{id}")
		public ResponseDto<Integer> deleteById(@PathVariable int id) {
			boardService.글삭제하기(id);
			return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
		}

boardService.글삭제하기(int id)가 동작한다.

	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
	}

## 글 수정하기
글을 작성한 사용자만 수정할 수 있음(수정 버튼 활/비활성화)

권한이 없는 사용자가 수정 경로를 직접 이동하였을 경우 수정할 수 없게끔 설정

<details>
<summary>글 수정 구현</summary>
<div markdown="1"> 
<image src="https://user-images.githubusercontent.com/83220871/139649106-f583c49f-da2b-401d-868a-103fa22fe308.gif" width="800" height="400"/>


	<c:choose>
			<c:when test="${board.user.id != principal.user.id}">
				<h1>잘못된 경로입니다.</h1>
			</c:when>
			...
	</c:choose>

</div>
</details>

상세보기에서 수정할 수 있는 화면으로 직접 이동(링크 설정)



	<c:if test="${board.user.id == principal.user.id}">
				<a href="/board/${board.id}/updateForm" class="btn btn-warning">수정</a>
	</c:if>

a href="/board/${board.id}/updateForm" 이동할 때 boardController.java의 GetMapping이 동작

	@GetMapping("/board/{id}/updateForm")
		public String updateForm(@PathVariable int id, Model model) {
			model.addAttribute("board", boardService.글상세보기(id));
			return "board/updateForm";
		}

updateForm에서 수정을 마친 후, 수정하기 버튼 클릭

글 작성자만 수정가능

	<c:if test="${board.user.id == principal.user.id}">
		<button id="btn-update" class="btn btn-primary">수정하기</button>
	</c:if>

${board.id} / ${board.title} / ${board.content} 의 값을 board.js에서 가져온다. 

=> 글쓰기폼 (saveForm)에서 기존의 데이터를 그대로 가져오기 위함

　
	$("#btn-update").on("click", () => {
				this.update();
			});

 	...

	update: function() {
		let id = $("#id").val();
		
		let data = {
			title: $("#title").val(),
			content: $("#content").val(),
		};

		$.ajax({
			type: "PUT",
			url: "/api/board/"+id,
			data: JSON.stringify(data),
			contentType: "application/json; charset=utf-8", 
			dataType: "json" 
		}).done(function() {
			alert("글 수정이 완료되었습니다.");
			location.href = "/";
		}).fail(function(error) { // 실패일때
			alert(JSON.stringify(error));
		});
	}

url: "/api/board/"+id을 통해 PutMapping이 동작하고 아래의 메소드가 동작한다.

	@PutMapping("api/board/{id}")
		public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board) {
			boardService.글수정하기(id, board);
			return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
		}


boardService.글수정하기호출

작성한 글 DB에 동기화 (영속화)

	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 찾기 실패: 아이디를 찾을 수 없습니다.");
				}); // 영속화 완료 (데이터베이스와 동기화가 되어있음)
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수로 종료시(Service가 종료될 때) 트랜잭션이 종료된다. => 더티체킹
		// 영속화 되어있는 board의 데이터가 달라졌기 때문에 더티체킹으로 DB쪽으로 자동 업데이트된다(flush가 된다) 
	}



## 회원정보수정

header.jsp의 회원정보를 클릭하면 회원정보를 수정할 수 있는 페이지로 이동

	<li class="nav-item"><a class="nav-link" href="/user/updateForm">회원정보</a></li>

UserController의 @GetMapping("/user/updateForm") 으로 return "user/updateForm"을 통해 ViewResolver를 열어준다.

	@GetMapping("/user/updateForm")
			public String updateForm() {
				return"user/updateForm"; 
			}

updateForm이 열리면 id의 경우 수정하지 못하게 readonly로 막아줌

	<input type="text" value="${principal.user.username}"class="form-control" placeholder="Enter username" id="username" readonly>

기존의 회원정보를 세션에서 그대로 불러오도록 하기위해 다음과 같이 처리한다. (선택)

	value="${principal.user.username}
	value="${principal.user.password}
	value="${principal.user.email}

정보를 변경한 뒤, 회원수정 버튼을 누르면 정보가 업데이트 되도록 user.js를 설정한다.

	$("#btn-update").on("click", () => {
				this.update();
			});
		
		...

		update: function() {
		let data = {
			id: $("#id").val(),	// password와 email을 수정할 때, 누구의 id 값인지 알아야 하므로 id정보도 알아야 함
			password: $("#password").val(),
			email: $("#email").val()
		};
		
		...

	$.ajax({
				type: "PUT",
				url: "/user",
				data: JSON.stringify(data),
				contentType: "application/json; charset=utf-8", 
				dataType: "json"
			}).done(function() {
				alert("회원 수정이 완료되었습니다.");
				location.href = "/";
			}).fail(function(error) {
				alert(JSON.stringify(error));
			});
		}


url: "/user", 에 해당하는 정보를 UserApiController의 @PutMapping("/user") 으로 만들어준다.

	@PutMapping("/user") 
	public ResponseDto<Integer> update(@RequestBody User user) { // RequestBody를 걸어주어야 JSON데이터를 받을 수 있음
		userService.회원수정(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}

UserService의 회원수정에 대한 정보를 만들어준다.

	@Transactional
		public void 회원수정(User user) {
			// 수정시 영속성 컨텍스트 User 오브젝트를 영속화, 영속화된 User 오브젝트를 수정하는 방식
			// select를 해서 User 오브젝트를 DB로 부터 가져오는 이유는 영속화를 하기 위해
			// 영속화된 오브젝트를 변경하면 자동으로 DB 업데이트가 됨
			User persistance = userRepository.findById(user.getId())
					.orElseThrow(()->{
				return new IllegalArgumentException("회원찾기 실패");
			});
			String rawPassword = user.getPassword(); // 변경된 비밀번호를 rawPassword에 넣어줌
			String encPassword = encoder.encode(rawPassword); // rawPassword를 암호화된 비밀번호로 변경
			persistance.setPassword(encPassword); 
			persistance.setEmail(user.getEmail());
			// 회원수정 함수 종료 시 = 서비스 종료 = 트랜잭션 종료 => 커밋 활성화
			// 영속화된 persistance 객체의 변화가 감지되면 더티체킹되어 update문을 날려준다.
		}

## 회원정보수정2 (강제 세션 값 변경)

<details>
<summary>기존의 회원정보수정</summary>
<div markdown="1"> 
<image src="https://user-images.githubusercontent.com/83220871/139832529-d0a5bca2-7cae-4992-a210-a9be45c226cd.gif" width="800" height="400"/>
</div>
</details>

이전의 회원정보수정에서는 수정을 하게 되면 서버에서는 수정된 정보가 바로 저장이 되지만, 

페이지 내에서는 수정 이전의 기존 정보가 그대로 나온다. 

트랜잭션 종료 즉시, DB는 업데이트가 되지만, 세션 값은 변하지 않기 때문이다.

따라서 강제로 세션 값을 변경해주어야 한다.

#### 기존 Spring Security 생성 개념

계정 생성 -> 필터가 가로채 토큰 생성 

-> Authentication Manger에게 던져서 Authentication 객체 생성(조건: DB에 값이 있는 경우)

-> 세션에 저장 (Security Context는 user 오브젝트를 저장하지 못함 즉, Authentication 객체만 저장) 


#### 세션 강제하기

간단히 말해, 직접 Authentication 객체를 만들어 세션에 저장하면 된다.

그러기 위해 Authentication Manager에 직접 접근하여 강제 로그인을 한 뒤,

Authentication 객체를 만들면 자동으로 SecurityContext(세션)에 값을 넣어준다.

	UserApiController.java

	Authentication authentication = authenticationManger.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
			SecurityContextHolder.getContext().setAuthentication(authentication);

user.getUsername()을 받기 위해 user.js에서 username 값을 지정해야 한다.

	update: function() {
		let data = {
			id: $("#id").val(),
			username: $("#username").val(),
			password: $("#password").val(),
			email: $("#email").val()
		};

강제로 세션 변경

	SecurityContextHolder.getContext().setAuthentication(authentication);

## OAuth login

### OAuth(Open Auth) : 
* 인증 처리를 대신 해줌 (인증 서버의 callback을 통해)
* 인증 완료 후 access 토큰을 부여하여 접근 권한을 위임

### 기존 로그인 환경 :

클라이언트 요청 - 서버의 응답

### OAuth 로그인 방식 :

	리소스 오너(클라이언트) - 클라이언트(서버) - 인증서버 - 리소스서버

* 리소스 오너: 인증서버에게 로그인 요청
* 클라이언트: callback을 받으면 인증처리 완료 / 인증서버에게 자원서버의 접근 권한요청
* 인증서버: 로그인요청의 응답으로 클라이언트에게 코드를 callback / access 토큰을 응답(클라이언트에게 권한 부여)
* 리소스 서버: 정보를 가지고 있는 서버(DB)

		클라이언트는 인증서버에게 callback 코드를 받으면 인증처리 완료
		클라이언트는 인증서버에게 access 토큰을 받으면 권한부여
		=> 결과적으로 리소스 오너는 클라이언트에게 리소스 서버의 접근 권한을 위임


## Kakao OAuth2.0

카카오 API를 통해 카카오 로그인 방식을 구현

카카오 로그인 시 자동 OAuth 회원가입 및 로그인이 되도록 함

cosKey를 사용(OAuth계정의 마스터키)

	Application.yml

	cos:
	  key: (암호입력) # 카카오로그인 하는사람들의 회원가입시 패스워드를 고정

#### cosKey가 노출되면 전체 OAuth계정의 피해를 입을 수 있으므로 절대 노출x

카카오 로그인과 일반 로그인의 사용자를 구분

user클래스에서 private String oauth 필드 생성

카카오계정 => kakao / 일반계정 => empty

cosKey가 노출되면 카카오 OAuth계정을 알아내어 회원정보를 바꿀 수 없도록

updateForm에서 oauth가 empty인 경우(일반회원)만 회원정보를 수정할 수 있게함

	<c:if test="${not empty principal.user.oauth}">
			<div class="form-group">
				<label for="password">Password</label>
				<input type="password" class="form-control" placeholder="Enter password" id="password">
			</div>
			</c:if>

서버쪽에서 postman과 같은 post공격에 대비도 필요

	UserService.java

	// Validate 체크 => oauth에 값이 없으면 수정 가능
			if(persistance.getOauth() == null || persistance.getOauth().equals("")) {
				String rawPassword = user.getPassword(); // 변경된 비밀번호를 rawPassword에 넣어줌
				String encPassword = encoder.encode(rawPassword); // rawPassword를 암호화된 비밀번호로 변경
				persistance.setPassword(encPassword); 
				persistance.setEmail(user.getEmail());
				// 회원수정 함수 종료 시 = 서비스 종료 = 트랜잭션 종료 => 커밋 활성화
				// 영속화된 persistance 객체의 변화가 감지되면 더티체킹되어 update문을 날려준다.
			}
