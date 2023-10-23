package com.dev.community.domain.posts;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
	
	// title로 조회
	Posts findByTitle(String title);

	// title, content로 조회
	Posts findByTitleAndContent(String title, String content);

	// 전체 리스트(id DESC)
	@Query("SELECT p FROM Posts p ORDER BY p.id DESC")
	List<Posts> findAllDesc();

}
