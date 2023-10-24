package com.dev.community.web.dto.posts.response;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.comment.response.CommentResponseDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Setter
@Getter
public class PostsResponseDTO {
	
	// 게시글 1개 조회 DTO
	private Integer id;
	private String title;
	private String content;
	private LocalDateTime createdDate;
	
	// 댓글 추가
	private List<CommentResponseDTO> commentList = new ArrayList<>();
	
	// 작성자 추가
	private Users author;
	
	// entity -> dto
	public PostsResponseDTO(Posts entity) {
		this.id = entity.getId();
		this.title = entity.getTitle();
		this.content = entity.getContent();
		this.createdDate = entity.getCreatedDate();
		// 댓글
		this.commentList = entity.getCommentList().stream()
				.map(comment -> CommentResponseDTO.CommentFactory(comment))
				.collect(Collectors.toList());
		// 작성자
		this.author = entity.getAuthor();
			
	}
	
	public static PostsResponseDTO PostsFactory(Posts entity) {
		PostsResponseDTO postsResponseDTO = new PostsResponseDTO(entity);
		return postsResponseDTO;
	}
	

}
