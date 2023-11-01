package com.dev.community.web.dto.posts.request;

import java.time.LocalDateTime;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Getter
public class PostsUpdateRequestDTO {
	
	private Integer id;
	@NotEmpty(message = "제목을 입력하세요.")
	private String title;
	@NotEmpty(message = "내용을 입력하세요.")
	private String content;
	private Users author;
	private LocalDateTime modifiedDate;
	private Integer viewCount;
	
	// GetMapping에서 수정화면에 기존 정보 넘겨주는 메소드
	public PostsUpdateRequestDTO toForm(String title, String content) {
		this.title = title;
		this.content = content;
		return this;
	}

	public Posts toEntity() {
		// TODO dto to entity
		return Posts.builder()
				.title(title)
				.content(content)
				.viewCount(viewCount)
				.build();
	}

	// entity to dto
	public PostsUpdateRequestDTO(Posts entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.modifiedDate = entity.getModifiedDate();
		this.author = entity.getAuthor();
		this.viewCount = entity.getViewCount();
	}
	
	public static PostsUpdateRequestDTO PostsFactory(Posts entity) {
		PostsUpdateRequestDTO postsUpdateRequestDTO = new PostsUpdateRequestDTO(entity);
		return postsUpdateRequestDTO;
	}
	

}
