/*
 * BU Term project for cs622
 
 This class stores the authentication request data  
 * @author  Harish Janardhanan * 
 * @since   20-Jan-2022
 */
package com.helpfinder.model.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class AuthRequest {
	//The user name to authenticate
	@NotBlank
	@Email
	private String email;

	//The password to authenticate
	@NotBlank
	private String password;

	/***
	 * gets the Email
	 * @return String the Email
	 */
	public String getEmail() {
		return email;
	}
	
	/***
	 * sets the Email* 
	 */

	public void setEmail(String email) {
		this.email = email;
	}
	
	/***
	 * gets the password
	* @return String the user password
	*/
	public String getPassword() {
		return password;
	}
	
	
	/***
	 * sets the User password	 * 
    */
	public void setPassword(String password) {
		this.password = password;
	}

}
