package com.auction.dto;

import javax.validation.constraints.NotBlank;

public class LoginDTO {

	@NotBlank(message = "UsernameOrEmail field may not be blank or null")
	private String usernameOrEmail;

	@NotBlank(message = "Password may not be blank or null")
	private String password;

	public String getUsernameOrEmail() {
		return usernameOrEmail;
	}

	public void setUsernameOrEmail(String usernameOrEmail) {
		this.usernameOrEmail = usernameOrEmail;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
