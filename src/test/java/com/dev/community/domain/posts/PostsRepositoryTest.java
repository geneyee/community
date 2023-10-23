package com.dev.community.domain.posts;

import java.time.LocalDateTime;
import java.util.List;

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
		for (int i = 1; i <= 300; i++) {
			String title = String.format("테스트 데이터 입니다. [%03d]", i);
			String content = String.format("%03d번 글 테스트 데이터 입니다.", i);

			postsRepository.save(Posts.builder()
					.title(title)
					.content(content)
					.createdDate(LocalDateTime.now())
					.build());
		}
		
		// when
		List<Posts> postsList = postsRepository.findAllDesc();
		
		// then
//		Posts posts = postsList.get(0);
		
	}

}
