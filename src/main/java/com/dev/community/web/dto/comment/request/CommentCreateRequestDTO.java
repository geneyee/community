package com.dev.community.web.dto.comment.request;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.user.Users;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class CommentCreateRequestDTO {
	
	private Integer id;
	private Integer postsId;
	private Users author;
	@DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	private LocalDateTime createdDate;
	
	@NotEmpty(message = "내용은 필수항목입니다.")
	private String content;
	
	// entity to dto
	public CommentCreateRequestDTO(Comment entity) {
		this.id = entity.getId();
		this.postsId = entity.getPosts().getId();
		this.content = entity.getContent();
		this.author = entity.getAuthor();
		this.createdDate = entity.getCreatedDate();
	}
	
	public static CommentCreateRequestDTO CommentFactory(Comment entity) {
		CommentCreateRequestDTO commentCreateRequestDTO = new CommentCreateRequestDTO(entity);
		return commentCreateRequestDTO;
	}

}
