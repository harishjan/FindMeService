
/*
 * BU Term project for cs622
 
 User related information is accessed through this controller
 * @author  Harish Janardhanan * 
 * @since   23-Jan-2022
 */
package com.helpfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.SecureUserDetails;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.service.UserService;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/user")

// User related information is accessed through this controller
public class UserController {
    
    @Autowired
    UserService<BasicUser> userService;
    
    
    /**
     * This method updates the workerSkills, the existing skill will be replaced.
     * @return WorkerUser the user which is created
     */
    @RequestMapping(value = "/updateWorkSkills", method = RequestMethod.POST, consumes = "application/json", produces = "application/json" )
    public WorkerUser updateWorkUserSkills(@RequestBody List<WorkerSkill> workerSkill) {
        //not implement as part of this project
        return null;
    }
    
    /**
     * update existing user information
     * @return HelpFinderUser the user which is created
     */
     @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public BasicUser updateUserInfo(@RequestBody BasicUser user) {
         // not implementing as part of this project
        return null;
    }
     
     /**
     * get user info of the current logged-in user, only a logged-in user with valid jwt session can access this method
     * @return User 
     */     
    @GetMapping(value = "/getUserInfo")
    @PreAuthorize("hasAuthority('ADD_ADMIN_USER') or hasAuthority('DELETE_USER') or hasAuthority('SEARCH_FOR_WORKERS') or hasAuthority('REVIEW_SITE_FEEDBACK') or hasAuthority('ARCH_SITE_FEEDBACK') or hasAuthority('ADD_ADMIN_USER') or hasAuthority('ALLOWED_TO_BE_HIRE')")
  //@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_HELPFINDER_USER') or hasAuthority('ROLE_WORKER_USER')") 
    public ResponseEntity<?> getUserInfo( ) {     
         SecureUserDetails secUser = userService.getAutenticatedUser();
         BasicUser user = (BasicUser)userService.findByUsername(secUser.getEmail());
         return user == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found") : ResponseEntity.ok(user);        
    }
    
    @GetMapping(value = "/getUserInquiriesSubmission")
    //@PreAuthorize("hasAuthority('ROLE_ADMIN') or hasAuthority('ROLE_MODERATOR') or hasAuthority('ROLE_HELPFINDER_USER') or hasAuthority('ROLE_WORKER_USER')")
    public ResponseEntity<?> getUserInquiriesSubmission()
    {
            return null;
    }
}
