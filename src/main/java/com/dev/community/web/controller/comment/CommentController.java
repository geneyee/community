package com.dev.community.web.controller.comment;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.community.domain.user.Users;
import com.dev.community.service.comment.CommentService;
import com.dev.community.service.posts.PostsService;
import com.dev.community.service.user.UserService;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;
import com.dev.community.web.dto.comment.request.CommentUpdateRequestDTO;
import com.dev.community.web.dto.comment.response.CommentResponseDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
	
	private final CommentService commentService;
	private final UserService userService;
	private final PostsService postsService;
	
	// 댓글 저장
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/create/{id}") // id-post_id
	public String save(@PathVariable Integer id, @Valid CommentCreateRequestDTO requestDTO, BindingResult bindingResult, Principal principal, Model model) {

		// posts id로 posts 조회
		PostsResponseDTO postsResponseDTO = postsService.findById(id);

		// 댓글 validation
		if (bindingResult.hasErrors()) {
			model.addAttribute("responseDTO", postsResponseDTO);
			return "post/read";
		}
		
		Users user = this.userService.getUser(principal.getName());
		this.commentService.save(id, requestDTO, user);
		
		return String.format("redirect:/posts/%s", id);
	}
	
	// 수정 화면
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/modify/{id}")
	public String modify(@PathVariable Integer id, Principal principal, 
			CommentUpdateRequestDTO commentUpdateRequestDTO, Model model) {
		
		CommentResponseDTO commentResponseDTO = this.commentService.findById(id);
		
		if(!commentResponseDTO.getUserResponseDTO().getUsername().equals(principal.getName())) {
//				.getAuthor().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		
		commentUpdateRequestDTO.toForm(commentResponseDTO.getContent());
		model.addAttribute("commentUpdateRequestDTO", commentUpdateRequestDTO);
		log.info("comment id => {}",commentUpdateRequestDTO.getId());
		
		return "post/comment_form";
	}
	
	// 수정
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/modify/{id}")
	public String modify(@Valid CommentUpdateRequestDTO commentRequestDTO, BindingResult bindingResult,
			@PathVariable Integer id, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			return "post/comment_form";
		}

		CommentUpdateRequestDTO dto = this.commentService.update(id, commentRequestDTO); 
		
		return "redirect:/posts/"+dto.getPostsId();
	}
	
	public String error422(String message, String location, Model model) {
		
		model.addAttribute(HttpStatus.UNPROCESSABLE_ENTITY);
		model.addAttribute("message", message);
		model.addAttribute("location", location);
		
		return "post/error";
	}

	// 삭제
	@PreAuthorize("isAuthenticated()")
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id, Principal principal) {
		log.info("id => {}", id);
		
		CommentResponseDTO responseDTO = this.commentService.findById(id);
		log.info("responseDTO => {}", responseDTO.toString());
		
		if(!responseDTO.getUserResponseDTO().getUsername().equals(principal.getName())) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		
		this.commentService.delete(responseDTO);
		
		return String.format("redirect:/posts/%s", responseDTO.getPostsId());
	}
	

}
