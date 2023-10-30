package com.dev.community.domain.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import jakarta.transaction.Transactional;

public interface PostsRepository extends JpaRepository<Posts, Integer> {
	
	// title로 조회
	Posts findByTitle(String title);

	// title, content로 조회
	Posts findByTitleAndContent(String title, String content);

	// 전체 리스트(id DESC)
	@Query("SELECT p FROM Posts p ORDER BY p.id DESC ")
	Page<Posts> findAllDesc(Pageable pageable);
	
	// 검색1

	Page<Posts> findAll(Specification<Posts> spec, Pageable pageable);
	
	// 검색2
	@Query("SELECT "
			+ "DISTINCT p "
			+ "FROM Posts p "
			+ "LEFT OUTER JOIN Users u1 ON p.author=u1 "
			+ "LEFT OUTER JOIN Comment c ON c.posts=p "
			+ "LEFT OUTER JOIN Users u2 ON c.author=u2 "
			+ "WHERE "
			+ "    p.title LIKE %:keyword% "
			+ "    OR p.content LIKE %:keyword% "
			+ "    OR u1.username LIKE %:keyword% "
			+ "    OR c.content LIKE %:keyword% "
			+ "    OR u2.username LIKE %:keyword% ")
	Page<Posts> findAllByKeyword(@Param("keyword") String keyword, Pageable pageable);

	// 조회수
	@Transactional
	@Modifying
	@Query("UPDATE Posts p SET p.viewCount = p.viewCount + 1 WHERE p.id = :id")
	Integer updateViewCount(@Param("id") Integer id);
}
