package com.dev.community.restapi.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.dev.community.domain.user.Users;
import com.dev.community.service.comment.CommentService;
import com.dev.community.service.user.UserService;
import com.dev.community.web.dto.comment.request.CommentCreateRequestDTO;
import com.dev.community.web.dto.comment.request.CommentUpdateRequestDTO;
import com.dev.community.web.dto.comment.response.CommentResponseDTO;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
public class CommentsController {
	
	private final CommentService commentService;
	private final UserService userService;
	
	// 댓글 목록 조회
	@GetMapping("/posts/{postsId}/comments")
	public ResponseEntity<List<CommentResponseDTO>> commentList(@PathVariable Integer postsId){
		
		List<CommentResponseDTO> responseDTO = this.commentService.commentList(postsId);
		
		return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
	}
	
	// 댓글 생성
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/posts/{postsId}/comments")
	public ResponseEntity<CommentCreateRequestDTO> create(@PathVariable Integer postsId, @RequestBody CommentCreateRequestDTO requestDTO
			, Principal principal){
		
		Users user = this.userService.getUser(principal.getName());
		
		CommentCreateRequestDTO createdDTO = this.commentService.save(postsId, requestDTO, user);
		
		return ResponseEntity.status(HttpStatus.OK).body(createdDTO);
	}
	
	// 댓글 수정
	@PreAuthorize("isAuthenticated()")
	@PatchMapping("/comment/{id}")
	public ResponseEntity<CommentUpdateRequestDTO> update(@PathVariable Integer id, @RequestBody CommentUpdateRequestDTO requestDTO,
			Principal principal) {
		
		CommentResponseDTO responseDTO = this.commentService.findById(id);
		
		if(!responseDTO.getUserResponseDTO().getUsername().equals(principal.getName())) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정권한이 없습니다.");
		}
		
		CommentUpdateRequestDTO updatedDTO = this.commentService.update(id, requestDTO);
		
		return ResponseEntity.status(HttpStatus.OK).body(updatedDTO);
	}
	
	// 댓글 삭제
	@PreAuthorize("isAuthenticated()")
	@DeleteMapping("/comment/{id}")
	public ResponseEntity<CommentUpdateRequestDTO> delete(@PathVariable Integer id, Principal principal) {
		
		CommentResponseDTO responseDTO = this.commentService.findById(id);
		log.info("responseDTO => {}", responseDTO.toString());
		
		
//		if(!responseDTO.getAuthor().getUsername().equals(principal.getName())) {
		if(!responseDTO.getUserResponseDTO().getUsername().equals(principal.getName())) {

			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제 권한이 없습니다.");
		}
		
		this.commentService.delete(responseDTO);
		
		return ResponseEntity.status(HttpStatus.OK).build();
	}

}
