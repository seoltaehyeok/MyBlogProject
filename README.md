# MyBlogProject

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
                .authorizeRequests() // request가 들어오면
                    .antMatchers("/auth/**", "/js/**", "/css/**", "/image/**") // /auth로 시작하는 것들은
                    .permitAll()		// 모두 허가
                    .anyRequest() // 다른 모든 요청은
                    .authenticated() // 인증이 필요함
                .and()
                    .formLogin()
                    .loginPage("/auth/loginForm"); // 인증이 필요할 경우 로그인 페이지로 이동
        }
    }

### 인증 필요
글쓰기(/board/form), 회원정보(/user/form), 로그아웃(/logout)

### 인증 불필요(/auth)
로그인(auth/loginForm), 회원가입(auth/joinForm), 회원가입 서버(auth/joinProc)

### 인증되지 않은 페이지 요청시 로그인 페이지로 이동
<img src ="https://user-images.githubusercontent.com/83220871/139435473-340e56f4-13f8-48ff-ab00-4080b51b1094.png" width="300" height="200"/>

### 비밀번호 해쉬(암호화)
    @Transactional // 하나의 로직이 됨 (전체가 성공하면 commit, 실패시 롤백)
	public void 회원가입(User user) {
		String rawPassword = user.getPassword(); // 1234원문
		String encPassword = encoder.encode(rawPassword); // 해쉬
		user.setPassword(encPassword); // 해쉬한 값을 비밀번호에 넣어줌
		user.setRole(RoleType.USER);
		userRepository.save(user);
	}
