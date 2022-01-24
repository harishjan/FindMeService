/***
 * This class is used for jwt user authentication 
 * 
 * reference taken from https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
 */


package com.helpfinder.model.request;
import javax.validation.constraints.Email;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
	@NotBlank
	@Email
	private String email;

	@NotBlank
	private String password;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
