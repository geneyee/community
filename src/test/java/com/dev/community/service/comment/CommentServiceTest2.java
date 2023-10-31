package com.dev.community.service.comment;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.dev.community.domain.comment.Comment;
import com.dev.community.domain.comment.CommentRepository;
import com.dev.community.web.dto.comment.response.CommentResponseDTO;

@ExtendWith(MockitoExtension.class)
public class CommentServiceTest2 {
	
	@InjectMocks
	private CommentService commentService;
	
	@Mock
	private CommentRepository commentRepository;
	
	/*
	@Test
	void delete() {
		// given
		Integer id = 46;
		Optional<Comment> comment = this.commentRepository.findById(id);
		assertTrue(comment.isPresent());
		Comment target = comment.get();
		
		CommentResponseDTO responseDTO = this.commentService.findById(id);
		
		// when
		this.commentService.delete(responseDTO);
		
		// then
		verify(this.commentRepository).delete(target);
	}*/

}
