package com.dev.community.web.controller.comment;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.dev.community.domain.user.Users;
import com.dev.community.service.comment.CommentService;
import com.dev.community.service.user.UserService;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;
import com.dev.community.web.dto.posts.response.PostsResponseDTO;

import lombok.RequiredArgsConstructor;

@RequestMapping("/comment")
@RequiredArgsConstructor
@Controller
public class CommentController {
	
	private final CommentService commentService;
	private final UserService userService;
	
	// 댓글 저장
//	@PostMapping("/create/{id}") // id-post_id
//	public String save(@PathVariable Integer id, @RequestParam String content) {
//		
//		CommentCreateRequestDTO requestDTO = this.commentService.save(id, content);
//		
//		return String.format("redirect:/posts/%s", id);
//	}
	
	@PostMapping("/create/{id}") // id-post_id
	public String save(@PathVariable Integer id, Model model, PostsResponseDTO responseDTO, @Validated CommentCreateRequestDTO createRequestDTO, BindingResult bindingResult, 
						RedirectAttributes rttr, Principal principal) {
		
		if(bindingResult.hasErrors()) {
			
			model.addAttribute("responseDTO", responseDTO);
			model.addAttribute("createRequestDTO", createRequestDTO);

			rttr.addFlashAttribute("msg", "댓글을 입력하세요.");
			rttr.addFlashAttribute("alertClass", "alert-danger");
			
			return String.format("redirect:/posts/%s", responseDTO.getId()); //id  
			
		}
		Users user = this.userService.getUser(principal.getName());
		CommentCreateRequestDTO requestDTO = this.commentService.save(id, createRequestDTO, user);
		
		return String.format("redirect:/posts/%s", requestDTO.getPostsId());
	}
	
	public String error422(String message, String location, Model model) {
		
		model.addAttribute(HttpStatus.UNPROCESSABLE_ENTITY);
		model.addAttribute("message", message);
		model.addAttribute("location", location);
		
		return "post/error";
	}

	

}
