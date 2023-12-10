package com.dev.community.web.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserCreateDTO {

	@Size(min = 2, max = 25)
	@NotBlank(message = "사용자 ID는 필수항목입니다.")
	private String username;
	
	@NotBlank(message = "비밀번호는 필수항목입니다.")
	private String password1;
	
	@NotBlank(message = "비밀번호 확인은 필수항목입니다.")
	private String password2;
	
	@NotBlank(message = "이메일은 필수항목입니다.")
	@Email
	private String email;

	@Builder
	public UserCreateDTO(String username, String password1, String password2, String email) {
		this.username = username;
		this.password1 = password1;
		this.password2 = password2;
		this.email = email;
	}
	
	
}
