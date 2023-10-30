package com.dev.community.web.dto.posts.request;

import java.time.LocalDateTime;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class PostCreateRequestDTO {

	// 글 저장 DTO
	@NotEmpty(message = "제목은 필수사항입니다.")
	@Size(max = 200)
	private String title;
	
	@NotEmpty(message = "내용은 필수사항입니다.")
	private String content;
	
	private Users author;
	private Integer viewCount = 0;
	
//	@Builder // 빌더 쓰면 null... 왜그럴까ㅠ 
//	public PostCreateRequestDTO(String title, String content) {
//		this.title = title;
//		this.content = content;
//	}
//	
	
	public Posts toEntity() {
		// TODO dto -> entity
		return Posts.builder()
				.title(title)
				.content(content)
				.createdDate(LocalDateTime.now())
				.author(author)
				.viewCount(viewCount)
				.build();
	}
	
	
}
