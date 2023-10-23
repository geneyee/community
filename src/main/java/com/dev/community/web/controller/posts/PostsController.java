package com.dev.community.web.controller.posts;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dev.community.domain.posts.Posts;
import com.dev.community.service.posts.PostsService;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;
import com.dev.community.web.dto.posts.request.PostCreateRequestDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/posts")
@Controller
public class PostsController {
	
	private final PostsService postsService;
	
	// 전체 조회 화면(index 화면)
	@GetMapping("/")
	public String index(Model model) {
		
		List<Posts> postsList = this.postsService.findAllDesc();
		model.addAttribute("postsList", postsList);
		
		return "post/index";
	}
	
	// 글쓰기 화면
	@GetMapping("/create")
	public String create(Model model) {
		
		model.addAttribute("postCreateRequestDTO", new PostCreateRequestDTO());
		
		return "post/posts_form";
	}
	
	// 글쓰기 기능
	@PostMapping("/create")
	public String save(@Valid PostCreateRequestDTO createRequestDTO, BindingResult bindingResult) {
		
		if(bindingResult.hasErrors()) {
			return "post/posts_form";
		}
		
		Integer id = this.postsService.save(createRequestDTO);
		
		return "redirect:/posts/";
	}
	
	// 글 1개 조회 화면 // 댓글 추가
	@GetMapping("/{id}")
	public String read(@PathVariable Integer id, Model model, CommentCreateRequestDTO requestDTO) {
		
		PostsResponseDTO responseDTO = this.postsService.findById(id);
		
		model.addAttribute("responseDTO", responseDTO);
		model.addAttribute("createRequestDTO", requestDTO);
		
		return "post/read";
	}
	
}
