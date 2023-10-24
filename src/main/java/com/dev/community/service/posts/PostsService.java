package com.dev.community.service.posts;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.posts.request.PostCreateRequestDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {
	
	private final PostsRepository postsRepository;

	public Page<Posts> findAllDesc(int page) {
		
		Pageable pageable = PageRequest.of(page, 10);
		return this.postsRepository.findAllDesc(pageable);
		
		// TODO 전체 리스트를 조회한다.
//		List<Posts> postsList = this.postsRepository.findAllDesc();
//		
//		return postsList;
	}

	public Integer save(PostCreateRequestDTO createRequestDTO, Users user) {
		// TODO 글을 저장한다.
		
		log.info("controller에서 넘어온 값 : {}", createRequestDTO.toString());
		log.info("controller에서 넘어온 값 : {}", user.getUsername());
		
		// service에서 찾은 username 넘겨준다.
		createRequestDTO.setAuthor(user);
		log.info("createRequestDTO.setAuthor(user) : {}", createRequestDTO.toString());
		
		// dto to entity
		Posts posts = createRequestDTO.toEntity();
		log.info("dto to entity : {}", posts.toString());
		
		// entity DB에 저장
		 Posts saved = this.postsRepository.save(posts);
		 log.info("entity DB에 저장 : {}", saved.toString());
		
		return saved.getId();
	}

	public PostsResponseDTO findById(Integer id) throws NoSuchElementException {
		// TODO 글을 조회한다.
		
		// 넘어온 id로 조회 
		Posts entity = this.postsRepository.findById(id).orElseThrow();
		
		// entity to dto
		return PostsResponseDTO.PostsFactory(entity);
	}

}
