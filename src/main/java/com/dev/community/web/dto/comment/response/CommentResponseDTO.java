package com.dev.community.web.dto.comment.response;

import java.time.LocalDateTime;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.user.UserResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class CommentResponseDTO {
	
	private Integer id;
	private Integer postsId;
	private String content;
	
	// 작성자 추가 - 안해주면 타임리프에서 게시글 조회할때 에러난다
//	private Users author;
	private UserResponseDTO userResponseDTO;
	
	// 작성자 추가 - 안해주면 타임리프에서 게시글 조회할때 에러난다
	private LocalDateTime createdDate;
	
	// 수정 날짜 표시
	private LocalDateTime modifiedDate;
	
	public CommentResponseDTO(Comment entity) {
		this.id = entity.getId();
		this.postsId = entity.getPosts().getId();
		this.content = entity.getContent();
//		this.author = entity.getAuthor();
		this.userResponseDTO = UserResponseDTO.of(entity.getAuthor());
		this.createdDate = entity.getCreatedDate();
		this.modifiedDate = entity.getModifiedDate();
	}
	
	public static CommentResponseDTO CommentFactory(Comment entity) {
		CommentResponseDTO commentResponseDTO = new CommentResponseDTO(entity);
		return commentResponseDTO;
	}

	// 삭제할 때
	public Comment toEntity() {
		// TODO dto to entity
		return Comment.builder()
				.id(id)
				.build();
	}

}
