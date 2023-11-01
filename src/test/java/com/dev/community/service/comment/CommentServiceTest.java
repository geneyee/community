package com.dev.community.service.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dev.community.domain.comment.CommentRepository;
import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;
import com.dev.community.web.dto.comment.request.CommentUpdateRequestDTO;
import com.dev.community.web.dto.comment.response.CommentResponseDTO;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
public class CommentServiceTest {
	
	@Autowired
	private CommentService commentService;
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostsRepository postsRepository;
	
	@Test
	@DisplayName("특정 게시글 id로 해당 글의 댓글 저장")
	@Transactional
	public void save() {
		
		//given
		// 회원-comment id
		Integer id = 46;
		// User 넘겨주기 위해서 기존에 있는 회원 아이디 찾아서 넘겨줌..
		Users user = this.commentRepository.findByAuthorId(id);
		
		// dto builder
		CommentCreateRequestDTO requestDTO = createDTO();
		
		Integer postsId = 1;
		
		// when
		CommentCreateRequestDTO savedDTO = this.commentService.save(postsId, requestDTO, user);
		
		// then
		assertThat(requestDTO.getPostsId()).isEqualTo(savedDTO.getPostsId());
	}
	

	
	
	@Test
	@DisplayName("특정 댓글 id로 해당 댓글 1개 조회")
	@Transactional
	void findById() {
		//given
		Integer commentId = 47;
		
		//when
		CommentResponseDTO responseDTO = this.commentService.findById(commentId);
		assertTrue(responseDTO != null);
		
		//then
		assertThat(responseDTO.getId()).isEqualTo(commentId);
	}
	
	@Test
	@DisplayName("특정 게시글 id로 전체 댓글 리스트 조회")
	@Transactional
	void commentList() {
		Integer postsId = 1;
		Optional<Posts> posts = this.postsRepository.findById(postsId);
		int expected = posts.get().getCommentList().size();
		log.info("expected => {}", expected);
		
		List<CommentResponseDTO> comments = this.commentService.commentList(postsId);
		log.info("comments => {}", comments.size());
	
		assertThat(comments.size()).isEqualTo(expected);
	}
	
	@Test
	@DisplayName("특정 댓글 id로 해당 댓글 수정")
	@Transactional
	void update() {
		Integer id = 46;
		CommentUpdateRequestDTO requestDTO = updateDTO();
		log.info("dto update => {}", requestDTO.getContent());
		
		CommentUpdateRequestDTO updated = this.commentService.update(id, requestDTO);
		log.info("service update => {}", updated.getContent());
		
		assertThat(updated.getContent()).isEqualTo(requestDTO.getContent());
	}
	
	@Test
	@DisplayName("특정 댓글 id로 해당 댓글 삭제")
	@Transactional
	void delete() {
		Integer id = 46;
		CommentResponseDTO responseDTO = this.commentService.findById(id);
		
		Boolean result = this.commentService.delete(responseDTO);
		
		assertThat(result).isTrue();
//		assertThat(result).isFalse();
	}

	
	// create dto
	private CommentCreateRequestDTO createDTO() {
		// comment id
		Integer id = 46;
		// User 넘겨주기 위해서 기존에 있는 회원 아이디 찾아서 넘겨줌..
		Users user = this.commentRepository.findByAuthorId(id);
		Integer postsId = 1;
		// 저장할 글 조회
		Optional<Posts> post = this.postsRepository.findById(postsId);
		Posts target = post.get();
		CommentCreateRequestDTO requestDTO = CommentCreateRequestDTO.builder()
				.postsId(target.getId())
				.author(user)
				.createdDate(LocalDateTime.now())
				.content("comment service 테스트 중입니다.")
				.build();
		return requestDTO;
	}
	
	// update dto
	private CommentUpdateRequestDTO updateDTO() {
		CommentCreateRequestDTO requestDTO = createDTO();
		
		CommentUpdateRequestDTO update= CommentUpdateRequestDTO.builder()
				.id(requestDTO.getId())
				.postsId(requestDTO.getPostsId())
				.modifiedDate(LocalDateTime.now())
				.content("update service 테스트 중입니다.")
				.build();
		return update;
	}
	
}
