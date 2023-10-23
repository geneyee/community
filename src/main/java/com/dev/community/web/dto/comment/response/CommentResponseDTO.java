package com.dev.community.web.dto.comment.response;

import com.dev.community.domain.comment.Comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {
	
	private Integer id;
	private Integer postsId;
	private String content;
	
	
	public CommentResponseDTO(Comment entity) {
		this.id = entity.getId();
		this.postsId = entity.getPosts().getId();
		this.content = entity.getContent();
	}
	
	public static CommentResponseDTO CommentFactory(Comment entity) {
		CommentResponseDTO commentResponseDTO = new CommentResponseDTO(entity);
		return commentResponseDTO;
	}

}
