package com.dev.community.domain.comment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
	
	@Query(value = "SELECT * FROM Comment WHERE posts_id = :postsId", nativeQuery = true)
	List<Comment> findByPostsId(@Param("postsId") Integer postsId);

}
