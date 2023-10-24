package com.dev.community.service.comment;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.comment.CommentRepository;
import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.UserRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.service.user.UserService;
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
	
	
	
	public CommentCreateRequestDTO save(Integer id, CommentCreateRequestDTO createRequestDTO, Users user)
			throws NoSuchElementException {
		log.info("comment controller to service id => {}", id);
		log.info("comment controller to service user => {}", user);
		// TODO 댓글을 저장한다.
		// 넘어온 id로 댓글 저장할 글 조회
		Posts posts = this.postsRepository.findById(id).orElseThrow();
		
		
		Comment comment = Comment.builder()
				.posts(posts)
				.content(createRequestDTO.getContent())
				.author(user)
				.createdDate(LocalDateTime.now())
				.build();
		
		log.info("user가 어떻게 저장되는지 확인 => {}", comment.toString());
		
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
