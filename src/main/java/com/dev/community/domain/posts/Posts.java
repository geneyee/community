package com.dev.community.domain.posts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.user.Users;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Entity
public class Posts {
	
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;

	@Column(length = 200)
	private String title;
	
	@Column(columnDefinition = "TEXT")
	private String content;
	
	@CreatedDate
	private LocalDateTime createdDate;
	
	@OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE)
	@Builder.Default
	private List<Comment> commentList = new ArrayList<>();
	
	@ManyToOne
	private Users author; // 글쓴이
	

	@Override
	public String toString() {
		return "Posts [id=" + id + ", title=" + title + ", content=" + content + ", createdDate=" + createdDate + "]";
	}

	
}
