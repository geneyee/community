package com.dev.community.web.controller.scrap;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.dev.community.domain.user.Users;
import com.dev.community.service.scrap.ScrapService;
import com.dev.community.service.user.UserService;
import com.dev.community.web.dto.scrap.ScrapDTO;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class ScrapController {
	
	private final ScrapService scrapService;
	private final UserService userService;
	
	
	@PreAuthorize("isAuthenticated()")
	@PostMapping("/posts/{id}/scrap")
	public ResponseEntity<ScrapDTO> scrap(@PathVariable Integer id, @RequestBody ScrapDTO scrapDTO, Principal principal){
		
		Users user = this.userService.getUser(principal.getName());
		
		ScrapDTO scraped = this.scrapService.scrap(id, scrapDTO, user);
		
		return ResponseEntity.status(HttpStatus.OK).body(scraped);
	}

}
