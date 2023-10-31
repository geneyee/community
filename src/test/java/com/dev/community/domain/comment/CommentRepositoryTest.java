package com.dev.community.domain.comment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.Users;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

//@DataJpaTest
@Slf4j
@SpringBootTest
public class CommentRepositoryTest {
	
	@Autowired
	private CommentRepository commentRepository;
	
	@Autowired
	private PostsRepository postsRepository;
	
	
	@Test
	@DisplayName("특정 게시글의 모든 댓글 조회")
	@Transactional
	void findByPostsId() {
		// postsId로 댓글리스트 바로 조회
		Integer id = 1;
		List<Comment> comments = this.commentRepository.findByPostsId(id);
		assertTrue(!comments.isEmpty());
		
		// postsId로 글 찾고 그 글의 댓글 리스트 조회
		Optional<Posts> posts = this.postsRepository.findById(1);
		assertTrue(posts.isPresent());
		int expected = posts.get().getCommentList().size();
		
		assertThat(comments.size()).isEqualTo(expected);
	}
	
	@Test
	@DisplayName("특정 댓글의 작성자 조회")
	void findByAuthor() {
		
		Integer id = 42;
		Users user = this.commentRepository.findByAuthorId(id);
		assertTrue(user != null);
		
		log.info("user => {}", user.toString());
		
		assertThat(user.getId()).isEqualTo(6);
		
	}
	
	@Test
	@DisplayName("댓글 저장")
	void save() {
		// 저장할 글 조회
		Integer postsId = 1;
		Optional<Posts> post = this.postsRepository.findById(postsId);
		assertTrue(post.isPresent());
		Posts p = post.get();
		
		Integer id = 42;
		Users user = this.commentRepository.findByAuthorId(id);
		
		Comment comment = Comment.builder()
				.content("댓글 저장 테스트 중입니다.")
				.posts(p)
				.author(user)
				.createdDate(LocalDateTime.now())
				.build();
		
		Comment saved =this.commentRepository.save(comment);
		
		assertThat(saved.getContent()).isEqualTo(comment.getContent());
	}
	
	@Test
	@DisplayName("특정 댓글 수정")
	void update() {
		
		// 수정할 댓글 조회
		Integer id = 46;
		Optional<Comment> comment = this.commentRepository.findById(id);
		assertTrue(comment.isPresent());
		Comment target = comment.get();
		
		
		 target = Comment.builder()
				.content("댓글 수정 테스트 중입니다.")
				.posts(target.getPosts())
				.author(target.getAuthor())
				.createdDate(target.getCreatedDate())
				.modifiedDate(LocalDateTime.now())
				.build();
		 
		 Comment updated = this.commentRepository.save(target);
		 
		 assertThat(updated.getContent()).isEqualTo(target.getContent());
		 assertThat(updated.getId()).isEqualTo(target.getId());
	}
	

}
