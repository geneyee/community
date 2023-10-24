package com.dev.community.domain.comment;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@Entity
public class Comment {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "posts_id")
	private Posts posts;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private Users author;
	
	@CreatedDate
	private LocalDateTime createdDate;

	@Override
	public String toString() {
		return "Comment [id=" + id + ", content=" + content + ", posts=" + posts + ", author=" + author
				+ ", createdDate=" + createdDate + "]";
	}

	

	
		
	

}
