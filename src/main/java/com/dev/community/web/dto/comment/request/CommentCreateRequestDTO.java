package com.dev.community.web.dto.comment.request;

import com.dev.community.domain.comment.Comment;

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
	
	@NotEmpty(message = "내용은 필수항목입니다.")
	private String content;
	
	// entity -> dto
	public CommentCreateRequestDTO(Comment entity) {
		this.id = entity.getId();
		this.postsId = entity.getPosts().getId();
		this.content = entity.getContent();
	}
	
	public static CommentCreateRequestDTO CommentFactory(Comment entity) {
		CommentCreateRequestDTO commentCreateRequestDTO = new CommentCreateRequestDTO(entity);
		return commentCreateRequestDTO;
	}

}
