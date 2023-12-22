package com.dev.community.service.posts;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.exception.DataNotFoundException;
import com.dev.community.web.dto.posts.request.PostCreateRequestDTO;
import com.dev.community.web.dto.posts.request.PostsUpdateRequestDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {
	
	private final PostsRepository postsRepository;
	
	
	// 검색
	private Specification<Posts> search(String keywords) {
		return new Specification<Posts>() {
			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Posts> p, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				// TODO 검색기능
				query.distinct(true); // 중복 제거
				Join<Posts, Users> u1 = p.join("author", JoinType.LEFT);
				Join<Posts, Comment> c = p.join("commentList", JoinType.LEFT);
				Join<Comment, Users> u2 = p.join("author", JoinType.LEFT);
				
				return criteriaBuilder.or(criteriaBuilder.like(p.get("title"), "%" + keywords + "%"), // 글 제목
						criteriaBuilder.like(p.get("content"), "%" + keywords + "%"), //  글 본문
						criteriaBuilder.like(u1.get("username"),  "%" + keywords + "%"), // 글 작성자
						criteriaBuilder.like(c.get("content"),  "%" + keywords + "%"), // 댓글 본문
						criteriaBuilder.like(u2.get("username"),  "%" + keywords + "%")); // 댓글 작성자
			}
		};
	}
	

	public Page<Posts> findAllDesc(int page, String keyword) {
		
		List<Sort.Order> sorts = new ArrayList<>();
		sorts.add(Sort.Order.desc("id"));
		Pageable pageable = PageRequest.of(page, 10, Sort.by(sorts));
		
//		Specification<Posts> spec = search(keyword);
		
//		return this.postsRepository.findAll(spec, pageable);
		
		return this.postsRepository.findAllByKeyword(keyword, pageable);
		
		// TODO 전체 리스트를 조회한다.
//		List<Posts> postsList = this.postsRepository.findAllDesc();
//		
//		return postsList;
	}

	public Posts save(PostCreateRequestDTO createRequestDTO, Users user) {
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
		
		return saved;
	}

	public PostsResponseDTO findById(Integer id) throws NoSuchElementException {
		// TODO 글을 조회한다.
		
		// 넘어온 id로 조회 
		Posts entity = this.postsRepository.findById(id).orElseThrow();
		
		// entity to dto
		return PostsResponseDTO.PostsFactory(entity);
	}
	
	// findById -> entity로 받기
	public Posts getPost(Integer id) {
		return this.postsRepository.findById(id).orElseThrow();
	}

	@PreAuthorize("isAuthenticated()")
	public PostsUpdateRequestDTO modify(Integer id, PostsUpdateRequestDTO dto, Principal principal) {
		
		log.info("id => {}", id);
		// TODO 글을 수정한다.
		
		// 1. url에 넘어온 id로 posts 데이터 찾기
		Posts target = this.postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("수정 실패! 대상 게시글이 없습니다."));
		
		// 2. 데이터가 없다면 예외 발생
//		if(target == null || id != target.getId()) {
//			log.info("잘못된 요청! id => {}, post => {}", id, target.toString());
//			throw new DataNotFoundException("User not found");
//		} 
		
		// 3. 찾은 데이터의 작성자id와 로그인 한 아이디가 다르면 예외발생
		if(!target.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		
		// 5. 업데이트
		Posts entity = target.update(dto);
		log.info("target.update(dto) => {}", entity.toString());
		
		Posts updated = this.postsRepository.save(entity);
		log.info("entity save, 날짜 저장 확인 => {}", updated.toString());
		
		// entity to dto
		return PostsUpdateRequestDTO.PostsFactory(updated);
	}
	
	// 삭제
	public Boolean delete(PostsResponseDTO responseDTO) {
		Posts posts = responseDTO.toEntity();
		this.postsRepository.delete(posts);
		return true;
	}

	public void vote(Posts posts, Users user) {
		// TODO 추천
		posts.getVoter().add(user);
		this.postsRepository.save(posts);
	}
	
	// 추천 - error : Found shared references to a collection ~
//	public void vote(PostsResponseDTO responseDTO, Users user) {
//		
//		posts.getVoter().add(user);
//		// getter대신 method..
//		responseDTO.toVote(user);
//		
//		// dto to entity
//		Posts entity = responseDTO.voteToEntity();
//		
//		this.postsRepository.save(entity);
//	}
	
	// 조회수
	public Integer updateView(Integer id) {
		return this.postsRepository.updateViewCount(id);
	}
	

	

}
