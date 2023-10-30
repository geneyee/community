package com.dev.community.web.controller.posts;

import java.security.Principal;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import com.dev.community.domain.posts.Posts;
import com.dev.community.domain.user.Users;
import com.dev.community.service.posts.PostsService;
import com.dev.community.service.user.UserService;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;
import com.dev.community.web.dto.posts.request.PostCreateRequestDTO;
import com.dev.community.web.dto.posts.request.PostsUpdateRequestDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/posts")
@Controller
public class PostsController {

	private final PostsService postsService;
	private final UserService userService;

	// 전체 조회 화면(index 화면)
	@GetMapping("/list")
	public String index(Model model, @RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "keyword", defaultValue = "")String keyword) {

		Page<Posts> paging = this.postsService.findAllDesc(page, keyword);
		model.addAttribute("paging", paging);
		model.addAttribute("keyword", keyword);

		return "post/index";
	}

	// 글쓰기 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/create")
	public String create(Model model) {

		model.addAttribute("postCreateRequestDTO", new PostCreateRequestDTO());

		return "post/posts_form";
	}

	// 글쓰기 기능
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create")
	public String save(@Valid PostCreateRequestDTO createRequestDTO, BindingResult bindingResult, Principal principal) {

		if (bindingResult.hasErrors()) {
			return "post/posts_form";
		}
		Users user = this.userService.getUser(principal.getName());
		Integer id = this.postsService.save(createRequestDTO, user);

		log.info("principal.getName() => {}", principal.getName());
		log.info("user => ", user.toString());

		return "redirect:/posts/list";
	}

	// 글 1개 조회 화면 // 댓글 추가
	@GetMapping("/{id}")
	public String read(@PathVariable Integer id, Model model, CommentCreateRequestDTO requestDTO) {

		PostsResponseDTO responseDTO = this.postsService.findById(id);
		this.postsService.updateView(id); // viewCount++

		model.addAttribute("responseDTO", responseDTO);
		model.addAttribute("createRequestDTO", requestDTO);
		
		return "post/read";
	}

	// 수정하기 화면
	@GetMapping("/modify/{id}")
	public String modify(@PathVariable Integer id, Principal principal, Model model,
			PostsUpdateRequestDTO postUpdateRequestDTO) {

		PostsResponseDTO responseDTO = this.postsService.findById(id);

		if (!responseDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}

		postUpdateRequestDTO.toForm(responseDTO.getTitle(), responseDTO.getContent());

		model.addAttribute("postUpdateRequestDTO", postUpdateRequestDTO);

		return "post/update_form";
	}

	// 수정하기
	@PostMapping("/modify/{id}")
	public String modify(@PathVariable Integer id, Principal principal,
			@Valid PostsUpdateRequestDTO postsUpdateRequestDTO, BindingResult bindingResult) {

		if (bindingResult.hasErrors()) {
			return "post/update_form";
		}

		PostsUpdateRequestDTO dto = this.postsService.modify(id, postsUpdateRequestDTO, principal);

		return "redirect:/posts/" + dto.getId();
	}

	// 삭제하기
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String delete(Principal principal, @PathVariable Integer id) {

		PostsResponseDTO responseDTO = this.postsService.findById(id);

		if (!responseDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}

		this.postsService.delete(responseDTO);

		return "redirect:/posts/list";
	}

	// 추천 - error : Found shared references to a collection ~
//	@PreAuthorize("isAuthenticated()")
//	@GetMapping("/vote/{id}")
//	public String vote(@PathVariable Integer id, Principal principal) {
//		
//		PostsResponseDTO responseDTO = this.postsService.findById(id);
//		Users user = this.userService.getUser(principal.getName());
//		
//		this.postsService.vote(responseDTO, user);
//		
//		return String.format("redirect:/posts/%s", id);
//	}
	
	// 추천
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/vote/{id}")
	public String vote(@PathVariable Integer id, Principal principal) {
		Posts posts = this.postsService.getPost(id);
		Users user = this.userService.getUser(principal.getName());
		
		this.postsService.vote(posts, user);
		
		return String.format("redirect:/posts/%s", id);
	}
}
