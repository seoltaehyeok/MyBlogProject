package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.User;

// @Repository 생략가능, DAO, 자동으로 bean등록
public interface UserRepository extends JpaRepository<User, Integer>{ // JpaRepository는 User테이블을 관리, pk는 Integer

}

// JPA Naming 쿼리 전략
// 방법 1 SELECT * FROM user WHERE username = ? AND password = ?; 쿼리동작 (?에는 순서대로 매개변수가 들어옴)
// User findByUsernameAndPassword(String username, String password);

//	//방법 2
//	@Query(value = "SELECT * FROM user WHERE username = ? AND password = ?", nativeQuery = true)
//	User login(String username, String password);