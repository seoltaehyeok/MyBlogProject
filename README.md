# MyBlogProject

# User
로그인 환경 제공

아이디(PK) / 패스워드 / 이메일 / 역할(ex. 관리자, 회원, 매니저) / 사용자명 / 현재시간
# Reply

# Board
게시판&게시물 환경 제공

아이디(PK) / 콘텐츠(게시물) / 조회수 / 사용자명(FK) / 시간표
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

# Workbanch
table create

<img src="https://user-images.githubusercontent.com/83220871/136510865-1f58d224-59e3-4b62-b257-b2dff0fca0a7.png" width="200" height="150"/> <img src="https://user-images.githubusercontent.com/83220871/136516718-d641c0dc-b1b8-4f5e-9cd7-d8adbe8bea30.png" width="200" height="150"/>
