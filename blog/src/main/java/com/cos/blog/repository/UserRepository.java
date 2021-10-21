package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.blog.model.User;

// @Repository 생략가능, DAO, 자동으로 bean등록
public interface UserRepository extends JpaRepository<User, Integer>{ // JpaRepository는 User테이블을 관리, pk는 Integer

}
