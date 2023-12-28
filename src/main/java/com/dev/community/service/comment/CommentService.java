package com.dev.community.service.comment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.comment.CommentRepository;
import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.UserRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.exception.DataNotFoundException;
import com.dev.community.service.user.UserService;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;
import com.dev.community.web.dto.comment.request.CommentUpdateRequestDTO;
import com.dev.community.web.dto.comment.response.CommentResponseDTO;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommentService {

	private final PostsRepository postsRepository;
	private final CommentRepository commentRepository;

	@Transactional
	public CommentCreateRequestDTO save(Integer id, CommentCreateRequestDTO createRequestDTO, Users user) {
		log.info("comment controller to service id => {}", id);
		log.info("comment controller to service user => {}", user);
		
		// TODO 댓글을 저장한다.
		// 넘어온 id로 댓글 저장할 글 조회
		Optional<Posts> posts = this.postsRepository.findById(id);

		if (posts.isPresent()) {
			Comment comment = Comment.builder()
					.posts(posts.get())
					.content(createRequestDTO.getContent())
					.author(user)
					.createdDate(LocalDateTime.now())
					.build();

			log.info("user가 어떻게 저장되는지 확인 => {}", comment.toString());

			Comment entity = this.commentRepository.save(comment);

			// entity to dto
			CommentCreateRequestDTO requestDTO = CommentCreateRequestDTO.CommentFactory(entity);
			
			return requestDTO;
		} else {
			throw new DataNotFoundException("posts not found" + id);
		}
	}

	@Transactional
	public CommentResponseDTO findById(Integer id) {
		// TODO 댓글 id로 댓글 데이터 조회
		Optional<Comment> entity = this.commentRepository.findById(id);

		if (entity.isPresent()) {
			// entity to dto
			return CommentResponseDTO.CommentFactory(entity.get());
		} else {
			throw new DataNotFoundException("comment not found");
		}
	}

	@Transactional
	public CommentUpdateRequestDTO update(Integer id, CommentUpdateRequestDTO commentRequestDTO) {
		
		// id로 업데이트 할 댓글 찾음
		Optional<Comment> entity = this.commentRepository.findById(id);
		
		if(entity.isPresent()) {
			Comment comment = entity.get();
			// 찾은 댓글 entity에 수정된 글 update
			comment.update(commentRequestDTO.getContent());
			
			// 저장
			Comment updated = this.commentRepository.save(comment);
			log.info("entity 확인 => {}",comment.toString());

			// entity to dto
			return CommentUpdateRequestDTO.CommentFactory(updated);
		} else {
			throw new DataNotFoundException("comment not found");
		}
	}

	@Transactional
	public Boolean delete(CommentResponseDTO responseDTO) {
		log.info("id => {}", responseDTO.getId());
	
		// TODO 삭제하기
		Optional<Comment> comment = this.commentRepository.findById(responseDTO.getId());
		
		if(comment.isPresent()) {
			// dto to entity
			Comment entity = responseDTO.toEntity();
			
			// delete
			this.commentRepository.delete(entity);
			return true;
		} else {
			throw new DataNotFoundException("comment not found");
		}
	}

	@Transactional
	// Rest API
	public List<CommentResponseDTO> commentList(Integer postsId) {
		// TODO 댓글 리스트 조회
		
		return this.commentRepository.findByPostsId(postsId).stream()
				.map(comment -> CommentResponseDTO.CommentFactory(comment))
				.collect(Collectors.toList());
	}
}
