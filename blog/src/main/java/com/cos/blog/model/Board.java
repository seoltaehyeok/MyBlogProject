package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Lob // 대용량 데이터 사용시
	private String content; // 섬머노트 라이브러리 사용 <html>태그 섞여서 디자인됨
	
	@ColumnDefault("0")
	private int count; // 조회수
	
	// fetch = FetchType.EAGER (Board를 select 할때도 user는 필요하므로 EAGER 사용)
	@ManyToOne(fetch = FetchType.EAGER) // Many = Board, User = One => 여러 게시물을 한명이 작성가능
	@JoinColumn(name="userId")
	private User user; // DB는 오브젝트를 저장x=>FK, 자바는 오브젝트 저장o
	
	// (Board를 select 할때도 reply는 필요하므로 EAGER 사용)
	@OneToMany(mappedBy = "board",fetch = FetchType.EAGER) // mappedBy 연관관계의 주인이 아니다 (FK가 아니다) DB에 칼럼을 만들지x
	private List<Reply> reply;
	
	@CreationTimestamp
	private Timestamp createDate;
}
