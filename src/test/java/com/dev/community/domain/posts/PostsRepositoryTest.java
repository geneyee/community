package com.dev.community.domain.posts;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PostsRepositoryTest {

	@Autowired
	private PostsRepository postsRepository;

	@DisplayName("테스트 데이터 생성")
	@Test
	void testJpa() {
		// given
		/*
		for (int i = 1; i <= 300; i++) {
			String title = String.format("테스트 데이터 입니다. [%03d]", i);
			String content = String.format("%03d번 글 테스트 데이터 입니다.", i);

			postsRepository.save(Posts.builder()
					.title(title)
					.content(content)
					.createdDate(LocalDateTime.now())
					.build());
		}*/
		
		// when
//		List<Posts> postsList = postsRepository.findAllDesc();
		
		// then
//		Posts posts = postsList.get(0);
		
	}
	
	@DisplayName("findAll")
	@Test
	void findAll() {
		List<Posts> list =  this.postsRepository.findAll();
		Posts posts = list.get(0);
		
		assertEquals(posts.getTitle(), "모두공간 이용 안내");
	}
	
	@DisplayName("findById")
	@Test
	void findById() {
		Optional<Posts> target = this.postsRepository.findById(1);
		if(target.isPresent()) {
			Posts post = target.get();
			assertEquals("모두공간 이용 안내", post.getTitle());
		}
	}
	
	@DisplayName("findByTitle")
	@Test
	void findByTitle() {
		Posts posts = this.postsRepository.findByTitle("모두공간 이용 안내");
		assertEquals(1, posts.getId());
	}
	
	@DisplayName("findByTitleAndContent")
	@Test
	void findByTitleAndContent() {
		Posts post = this.postsRepository.findByTitleAndContent("모두공간 이용 안내", "모두공간 안내 사항");
		assertEquals(1, post.getId());
	}
	
	
	

}
