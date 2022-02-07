
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
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpfinder.model.BasicUser;
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
        return null;
    }
    
    /**
     * update existing user information
     * @return HelpFinderUser the user which is created
     */
     @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public BasicUser updateUserInfo(@RequestBody BasicUser user) {
        return null;
    }
     
     /**
     * get user by id
     * @return User for the id given
     */     
     @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> getUser( @RequestParam int id) {         
         BasicUser user = (BasicUser)userService.getUser(id);
         return user == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found") : ResponseEntity.ok(user);        
    }
     @RequestMapping(value = "/getUserInquiriesSubmission", method = RequestMethod.GET)
     public ResponseEntity<?> getUserInquiriesSubmission()
     {
    	 return null;
     }
}
