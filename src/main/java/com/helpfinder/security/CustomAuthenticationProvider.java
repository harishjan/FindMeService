

/*
 * BU Term project for cs622
 
  This class implements the functionality required for AuthenticationProvider to validate user name and passowrd
 * @author  Harish Janardhanan * 
 * @since   27-jan-2022
 */

package com.helpfinder.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.helpfinder.model.SecureUserDetails;
import com.helpfinder.service.SecureUserService;

// this is a custom implementation for autentication provider to validate user name and password
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {
	
	@Autowired
	SecureUserService userService ;
	
	@Autowired
	PasswordEncoder encoder;
	
	
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		UsernamePasswordAuthenticationToken auth = (UsernamePasswordAuthenticationToken) authentication;
		  String username = String.valueOf(auth.getPrincipal());
		  String password = String.valueOf(auth.getCredentials());

		  // 1. Use the username to load the data for the user, including authorities and password.
		  SecureUserDetails user = (SecureUserDetails)userService.loadUserByUsername(username);

		  // validate the passowed
		  
		  if (!encoder.matches(password, user.getPassword())) {
		    throw new BadCredentialsException("Bad Credentials");
		  }
		  // clear password
		  user.setPassword("");

		  // 4. Return an authenticated token, 
		  return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()) ;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
	}
	
	

}
