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

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
	public String create(PostCreateRequestDTO postCreateRequestDTO) {

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
		Posts post = this.postsService.save(createRequestDTO, user);

		log.info("principal.getName() => {}", principal.getName());
		log.info("user => {}", user.toString());

		return "redirect:/posts/list";
	}

	// 글 1개 조회 화면 // 댓글 추가 // 조회수
	@GetMapping("/{id}")
	public String read(@PathVariable Integer id, Model model, CommentCreateRequestDTO requestDTO, HttpServletRequest request, HttpServletResponse response) {

		// viewCount++
		viewCount(id, request, response);
		//this.postsService.updateView(id); 
		PostsResponseDTO responseDTO = this.postsService.findById(id);
		model.addAttribute("responseDTO", responseDTO);
		model.addAttribute("createRequestDTO", requestDTO);
		
		return "post/read";
	}

	// 수정하기 화면
	@GetMapping("/modify/{id}")
	public String modify(@PathVariable Integer id, Principal principal, Model model,
			PostsUpdateRequestDTO postsUpdateRequestDTO) {

		PostsResponseDTO responseDTO = this.postsService.findById(id);

		if (!responseDTO.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}

		postsUpdateRequestDTO.toForm(responseDTO.getTitle(), responseDTO.getContent(), responseDTO.getId());

		model.addAttribute("postsUpdateRequestDTO", postsUpdateRequestDTO);

		return "post/update_form";
	}

	// 수정하기
	@PostMapping("/modify/{id}")
	public String modify(@PathVariable("id") Integer id, Principal principal,
			@Valid PostsUpdateRequestDTO postsUpdateRequestDTO, BindingResult bindingResult, Model model) {

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
	
	// 조회수 증가
	private void viewCount(Integer id, HttpServletRequest request, HttpServletResponse response) {
		// 쿠키 저장(name, value) -> viewPosts, [id]
		
		// 조회수 증가 쿠키 name
		final String COOKIE_NAME = "viewPosts";
		
		// 조회수 증가 완료
		Cookie viewedPostsCookie = null;
		
		// 쿠키를 조회해서 있으면 이름이 viewPosts인지 확인해서 맞다면 이미 조회수 증가 한 게시글이다. 
		Cookie[] cookies = request.getCookies();
		if(cookies != null) {
			for(Cookie cookie : cookies) {
				if(COOKIE_NAME.equals(cookie.getName())) {
					viewedPostsCookie = cookie;
				}
			}
		}
		
		// 조회수 증가 쿠키가 있다면 
		if(viewedPostsCookie != null) {
			if(!viewedPostsCookie.getValue().contains("["+ id + "]")) { // value에서 게시글 id를 조회해서 없다면 조회수 증가
				this.postsService.updateView(id); // 조회수 증가
				viewedPostsCookie.setValue(viewedPostsCookie.getValue() + "[" + id + "]"); // 조회수 증가 된 쿠키에 게시글 id를 추가
				viewedPostsCookie.setPath("/"); // 쿠키 유효 경로를 사이트 전체로 설정
				viewedPostsCookie.setMaxAge(60 * 60 * 24); // 쿠키 유효 시간을 24시간으로 설정
				response.addCookie(viewedPostsCookie); // 쿠키 저장
			}
		} else { // 쿠키가 없다면 조회수 증가 쿠키를 생성해서 저장
			this.postsService.updateView(id); // 조회수 증가
			Cookie newCookie = new Cookie(COOKIE_NAME, "[" + id + "]");
			newCookie.setPath("/");
			newCookie.setMaxAge(60 * 60 * 24);
			response.addCookie(newCookie);
		}
	}
}
