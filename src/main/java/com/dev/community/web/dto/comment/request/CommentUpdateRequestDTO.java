package com.dev.community.web.dto.comment.request;

import java.time.LocalDateTime;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.user.UserResponseDTO;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class CommentUpdateRequestDTO {

	private Integer id;
	private Integer postsId;
//	private Users author;
	private UserResponseDTO userResponseDTO;
	private LocalDateTime modifiedDate;

	@NotEmpty(message = "내용은 필수항목입니다.")
	private String content;

	// 기존 내용
	public CommentUpdateRequestDTO toForm(String content) {
		this.content = content;
		return this;
	}

	// dto to entity
	public Comment toEntity() {
		return Comment.builder()
				.content(content)
				.modifiedDate(LocalDateTime.now()).build();
	}

	// entity to dto
//	public CommentUpdateRequestDTO(Comment entity) {
//		this.id = entity.getId();
//		this.posts = entity.getPosts();
//		this.content = entity.getContent();
//		this.author = entity.getAuthor();
//		this.modifiedDate = entity.getModifiedDate();
//	}

//	public static CommentUpdateRequestDTO CommentFactory(Comment entity) {
//		CommentUpdateRequestDTO commentUdpateRequestDTO = new CommentUpdateRequestDTO(entity);
//		return commentUdpateRequestDTO;
//	}
	// entity to dto
	public static CommentUpdateRequestDTO CommentFactory(Comment entity) {
		return new CommentUpdateRequestDTO(
				entity.getId(), 
				entity.getPosts().getId(), 
				UserResponseDTO.of(entity.getAuthor()),
				entity.getContent(), 
				entity.getModifiedDate());
	}

	// @AllArgsConstructor
	public CommentUpdateRequestDTO(Integer id, Integer postsId, UserResponseDTO userResponseDTO, String content, LocalDateTime modifiedDate) {
		super();
		this.id = id;
		this.postsId = postsId;
		this.userResponseDTO = userResponseDTO;
		this.modifiedDate = modifiedDate;
		this.content = content;
	}
	
	

}
