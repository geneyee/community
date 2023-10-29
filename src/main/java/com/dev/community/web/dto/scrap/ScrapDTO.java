package com.dev.community.web.dto.scrap;

import com.dev.community.domain.scrap.Scrap;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@Getter
public class ScrapDTO {
	
	private Integer id;
	private Integer posts;
	@JsonProperty("users_id")
	private Long usersId;
	private Boolean status;
	
	// entity to dto
	public ScrapDTO(Scrap entity) {
		this.id = entity.getId();
		this.posts = entity.getPosts().getId();
		this.usersId = entity.getUsers().getId();
//		this.createdDate = entity.getCreatedDate();
		this.status = entity.getStatus();
	}
	
	public static ScrapDTO ScrapFactory(Scrap entity) {
		ScrapDTO scrapDTO = new ScrapDTO(entity);
		return scrapDTO;	
	}
}
