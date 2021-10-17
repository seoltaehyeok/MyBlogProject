# MyBlogProject

# 구현 CRUD

계정생성 / 이메일, 비밀번호 변경기능 / 페이지 / 예외처리

# User
로그인 환경 제공

아이디(PK) / 패스워드 / 이메일 / 역할(ex. 관리자, 회원, 매니저) / 사용자명 / 로그인시간
# Reply
게시물에 대한 답변 게시판 환경 제공

아이디(PK) / 콘텐츠(게시물) / 게시물의 답글(FK) / 답변자명(FK) / 답글 작성시간
# Board
게시판&게시물 환경 제공

아이디(PK) / 콘텐츠(제목) / 콘텐츠(게시물) / 조회수 / 작성자명(FK) / 작성시간 / 게시물의 답글(Board를 select 할때 reply 필요)
# yml
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

# Workbench
table create

<img src="https://user-images.githubusercontent.com/83220871/136510865-1f58d224-59e3-4b62-b257-b2dff0fca0a7.png" width="200" height="150"/> <img src="https://user-images.githubusercontent.com/83220871/136516718-d641c0dc-b1b8-4f5e-9cd7-d8adbe8bea30.png" width="200" height="150"/> <img src="https://user-images.githubusercontent.com/83220871/136522001-48107d3e-b86c-4a3a-89bf-4ea6f0152376.png" width="200" height="150"/>

# Keypoint

username 즉, 아이디의 경우 중복되면 안되므로 unique=true를 통해 고유값 지정
