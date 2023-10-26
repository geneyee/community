package com.dev.community.service.scrap;

import java.security.Principal;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.posts.PostsRepository;
import com.dev.community.domain.scrap.Scrap;
import com.dev.community.domain.scrap.ScrapRepository;
import com.dev.community.domain.user.Users;
import com.dev.community.web.dto.scrap.ScrapDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ScrapService {
	
	private final ScrapRepository scrapRepository;
	private final PostsRepository postsRepository;

	
	public ScrapDTO scrap(Integer id, ScrapDTO scrapDTO, Users user) {
		// TODO 스크랩
		
		// id로 게시글 조회
		Posts posts= this.postsRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("스크랩 실패. 대상 게시글이 없습니다."));
		
		if(scrapRepository.findByPostsAndUsers(posts, user) == null) {
			// 스크랩 처음이면 entity 생성 후 스크랩
			Scrap scrap = new Scrap(posts, user);
			Scrap entity = this.scrapRepository.save(scrap);
			
			// entity to dto
			return ScrapDTO.ScrapFactory(entity);
		} else {
			// 스크랩 했다면 취소(삭제)
			Scrap scrap = this.scrapRepository.findByPostsAndUsers(posts, user);
			scrap.cancelScrap(posts);
			this.scrapRepository.delete(scrap);
			
			return ScrapDTO.ScrapFactory(scrap);
		}

	}
	

}
