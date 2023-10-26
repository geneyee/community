package com.dev.community.domain.scrap;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.format.annotation.DateTimeFormat;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;

import groovy.transform.builder.Builder;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Entity
public class Scrap {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Users users;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "posts_id", nullable = false)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Posts posts;
	
	@Column(nullable = false)
	private Boolean status; // true - 스크랩 , false = 취소
	
	@DateTimeFormat(pattern = "yyyy-mm-dd")
	private LocalDate createdDate;
	
	@PrePersist //db에  insert 되기 전에 실행. db에 값을 넣으면 자동 실행
	public void createDate() {
		this.createdDate = LocalDate.now();
	}
	
	// 스크랩 
	public Scrap(Posts post, Users user) {
		this.posts = post;
		this.users = user;
		this.status = true;
		
		Posts.builder()
			.scrapCount(post.getScrapCount()+1)
			.build();
	}
	
	// 스크랩 취소
	public void cancelScrap(Posts post) {
		this.status = false;
		
		Posts.builder()
			.scrapCount(post.getScrapCount()-1)
			.build();
	}

}
