package com.helpfinder.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;


import com.helpfinder.security.jwt.JwtUtils;
import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserRole;
import com.helpfinder.model.SecureUserDetails;
import com.helpfinder.model.User;
import com.helpfinder.model.UserRole;
import com.helpfinder.model.request.AuthRequest;
import com.helpfinder.model.request.SignupRequest;
import com.helpfinder.model.response.JwtResponse;
import com.helpfinder.model.response.MessageResponse;
import com.helpfinder.service.UserService;
import com.helpfinder.repository.RoleRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserService userService;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody AuthRequest loginRequest) {

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
                         roles));
  }

  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    if (userService.findByUsername(signUpRequest.getEmail()) == null) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userService.existsByEmailAddress(signUpRequest.getEmail()) == null) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Error: Email is already in use!"));
    }

    // Create new user's account
    User user = new BasicUser();
    
    Set<String> strRoles = signUpRequest.getRole();
    Set<UserRole> userRoles = new HashSet<>();

    if (strRoles == null) {
    	UserRole userRole = roleRepository.findByName(EUserRole.ROLE_HELPFINDER_USER)
          .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
    	userRoles.add(userRole);
    } else {
      strRoles.forEach(role -> {
        switch (role) {
        case "admin":
        	UserRole adminRole = roleRepository.findByName(EUserRole.ROLE_ADMIN)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        	userRoles.add(adminRole);

          break;
        case "mod":
        	UserRole modRole = roleRepository.findByName(EUserRole.ROLE_MODERATOR)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        	userRoles.add(modRole);

          break;
        case "work":
            UserRole workerRole = roleRepository.findByName(EUserRole.ROLE_WORKER_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            userRoles.add(workerRole);

            break;
        default:
        	UserRole userRole = roleRepository.findByName(EUserRole.ROLE_HELPFINDER_USER)
              .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        	userRoles.add(userRole);
        }
      });
    }    
	user.setUserInformation("", "","", signUpRequest.getEmail(), userRoles);
	user.setPassword(encoder.encode(signUpRequest.getPassword()));	
    try
    {
    	userService.createUser(user);
    }
    catch (UserExistException e) {
		System.err.println("User exist in the system " + e.getMessage());
	    return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
	}

    return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
  }
}
