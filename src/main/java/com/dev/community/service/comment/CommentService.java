package com.dev.community.service.comment;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.comment.CommentRepository;
import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {
	
	private final PostsRepository postsRepository;
	private final CommentRepository commentRepository;
	
	
	public CommentCreateRequestDTO save(Integer id, CommentCreateRequestDTO createRequestDTO)
			throws NoSuchElementException {
		log.info("{}", id);
		// TODO 댓글을 저장한다.
		// 넘어온 id로 댓글 저장할 글 조회
		Posts posts = this.postsRepository.findById(id).orElseThrow();
		
		Comment comment = Comment.builder()
				.posts(posts)
				.content(createRequestDTO.getContent())
				.build();
		
		Comment entity = this.commentRepository.save(comment);

		// entity to dto
		CommentCreateRequestDTO requestDTO = CommentCreateRequestDTO.CommentFactory(entity);
		return requestDTO;

	}

	/*
	public CommentCreateRequestDTO save(Integer id, String content) throws NoSuchElementException {
		// TODO 댓글을 저장한다.
		
		// 넘어온 id로 댓글 저장할 글 조회
		Posts posts = this.postsRepository.findById(id).orElseThrow();
		
		// 댓글 저장
		Comment comment = Comment.builder()
				.posts(posts)
				.content(content)
				.build();
		
		Comment entity = this.commentRepository.save(comment);
		
		// entity to dto
		CommentCreateRequestDTO requestDTO = CommentCreateRequestDTO.CommentFactory(entity);
		return requestDTO;
	}*/
	
	

}
