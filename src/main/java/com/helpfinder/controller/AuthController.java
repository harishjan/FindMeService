
/*
 * BU Term project for cs622
 
 This class is used for exposing the apis required for User authentication and registration   
 * @author  Harish Janardhanan * 
 * @since   23-Jan-2022
 */

package com.helpfinder.controller;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.RepositoryCreationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.helpfinder.security.jwt.JwtUtils;
import com.helpfinder.exception.InvalidAddressException;
import com.helpfinder.exception.InvalidPasswordException;
import com.helpfinder.exception.InvalidSkillException;
import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.SecureUserDetails;
import com.helpfinder.model.request.AuthRequest;
import com.helpfinder.model.request.SignupRequest;
import com.helpfinder.model.response.JwtResponse;
import com.helpfinder.model.response.MessageResponse;
import com.helpfinder.service.UserService;


// This class is used for exposing the apis required for User authentication and registration  
//This is not implementation completely
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserService<BasicUser> userService;
  
  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  /***
   * User sign-in though this endpoint
   * @param loginRequest the login details
   * @return JwtResponse jwt token and user details
   */
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {
         
       try
       {

           Authentication authentication = authenticationManager.authenticate(
               new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
       
           SecurityContextHolder.getContext().setAuthentication(authentication);
           String jwt = jwtUtils.generateJwtToken(authentication);
           
           SecureUserDetails userDetails = (SecureUserDetails) authentication.getPrincipal();    
           List<String> roles = userDetails.getAuthorities().stream()
               .map(item -> item.getAuthority())
               .collect(Collectors.toList());
       
           return ResponseEntity.ok(new JwtResponse(jwt, 
                                userDetails.getId(), 
                                userDetails.getUsername(), 
                                userDetails.getEmail(), 
                                userDetails.getFirstName(), 
                                userDetails.getLastName(),
                                roles));
       }
       catch(BadCredentialsException | UsernameNotFoundException  ex){
           System.err.println("Invalid UserName or Password " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid UserName or Password");
    }
  }

  /***
   * User sign-up as a basic user to the system using this endpoint 
   * @param signUpRequest the request with the details required for sign-up
   * @return BasicUser the instance of user if successful
   */
  @PostMapping("/signup")
  public ResponseEntity<?> registerBsicUser(@Valid @RequestBody SignupRequest signUpRequest) {
       return registerUser(signUpRequest, EUserType.ROLE_HELPFINDER_USER);    
  }
  
  
  /***
   * User sign-up as worker to the system using this endpoint 
   * @param signUpRequest the request with the details required for sign-up
   * @return BasicUser the instance of user if successful
   */
  @PostMapping("/signup/registerWorker/")
  public ResponseEntity<?> registerWorker(@Valid @RequestBody SignupRequest signUpRequest) {
       return registerUser(signUpRequest, EUserType.ROLE_WORKER_USER);    
  }
  
  
  /***
   * User sign-up as admin to the system using this endpoint 
   * only an admin user can create an admin users
   * @param signUpRequest the request with the details required for sign-up
   * @return BasicUser the instance of user if successful
   */
  @PostMapping("/signup/registerAdmin/")
  @PreAuthorize("hasAuthority('ADD_ADMIN_USER')")
  public ResponseEntity<?> registerAdmin(@Valid @RequestBody SignupRequest signUpRequest) {
       return registerUser(signUpRequest, EUserType.ROLE_ADMIN);
  }
  
  
  /***
   * User sign-up as moderator to the system using this endpoint 
   * only an admin or a moderator can create an moderator user
   * @param signUpRequest the request with the details required for sign-up
   * @return BasicUser the instance of user if successful
   */
  @PostMapping("/signup/registerModerator/")
  @PreAuthorize("hasAuthority('ARCH_SITE_FEEDBACK') or hasAuthority('ADD_ADMIN_USER')")  
  public ResponseEntity<?> registerModerator(@Valid @RequestBody SignupRequest signUpRequest) {
         return registerUser(signUpRequest, EUserType.ROLE_MODERATOR);
  }

  
  /***
   * Common method to register users
   * @param signUpRequest
   * @param userType
   * @return
   */
  private ResponseEntity<?> registerUser(SignupRequest signUpRequest, EUserType userType)  
  {
           if (userService.existsByEmailAddress(signUpRequest.getEmail()) != null) {
             return ResponseEntity
                 .badRequest()
                 .body(new MessageResponse("Error: Email is already in use!"));
           }

           // Create new user's account           
           try {
               userService.createUser(signUpRequest, userType);              
           }
           catch (UserExistException e) {
               System.err.println("User exist in the system " + e.getMessage());
               return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
           }
           catch(RepositoryCreationException | InvalidAddressException | InvalidPasswordException | InvalidSkillException ex ){
                  System.err.println("Error creating user " + ex.getMessage());
               return ResponseEntity.badRequest().body(new MessageResponse(ex.getMessage()));
           
           }

           return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
  
}
