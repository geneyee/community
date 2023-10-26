package com.dev.community.domain.scrap;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;

public interface ScrapRepository extends JpaRepository<Scrap, Integer>{
	
	// 스크랩 여부 확인
	Scrap findByPostsAndUsers(Posts post, Users users);

}
