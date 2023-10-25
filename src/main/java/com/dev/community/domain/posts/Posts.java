package com.dev.community.domain.posts;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.posts.request.PostsUpdateRequestDTO;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;


@ToString
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
	
	@ToString.Exclude
	@OneToMany(mappedBy = "posts", cascade = CascadeType.REMOVE, orphanRemoval = true)
	@Builder.Default
	private List<Comment> commentList = new ArrayList<>();
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "author_id")
	private Users author; // 글쓴이
	
	@LastModifiedDate
	private LocalDateTime modifiedDate;

	
	// update method
	public Posts update(PostsUpdateRequestDTO dto) {
		if(dto.getTitle() != null) {
			this.title = dto.getTitle();
		}
		if(dto.getClass() !=null) {
			this.content = dto.getContent();
		}
		this.modifiedDate = LocalDateTime.now();
		return this;
	}
	
	// 삭제할 때 dto to entity 시 쓸 것
	public Posts(Posts entity) {
		this.id = entity.id;
		this.author = entity.author;
	}


}
