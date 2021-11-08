package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity	// User 클래스가 MySQL에 테이블이 생성됨
// @DynamicInsert : Insert시 null인 필드(컬럼)를 제외시켜준다.
public class User {

	@Id //Primary key
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id; // Oracle: 시퀀스 / mysql: auto_increment
	
	@Column(nullable = false, length = 100, unique= true) // 아이디의 경우 중복되면 안되므로 unique=true로 고유값지정
	private String username; // 아이디
	

	private String oauth; // kakao, google
	
	@Column(nullable = false, length = 100)
	private String password;
	
	@Column(nullable = false, length = 50, unique=true) // 이메일의 경우 중복되면 안되므로 unique=true로 고유값지정
	private String email;
	
	// @ColumnDefault("user")
	// DB는 RoleType이 없으므로 어노테이션을 붙여준다.
	@Enumerated(EnumType.STRING)
	private RoleType role; // Enum을 쓰는게 좋다. => 도메인설정(주어진 범위내에서) 관리권한 (ADMIN, USER)
	
	@CreationTimestamp // 시간이 자동으로 입력
	private Timestamp createDate;
}
