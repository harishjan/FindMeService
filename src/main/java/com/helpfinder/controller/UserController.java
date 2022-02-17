
/*
 * BU Term project for cs622
 
 User related information is accessed through this controller
 * @author  Harish Janardhanan * 
 * @since   23-Jan-2022
 */
package com.helpfinder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.SecureUserDetails;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.model.request.SignupRequest;
import com.helpfinder.model.request.WorkInquiryRequest;
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
    public ResponseEntity<?> updateWorkUserSkills(@RequestBody List<WorkerSkill> workerSkill) {
        //not implement as part of this project
        return null;
    }
    
    /**
     * update existing user information,
     * Note this is not implemented
     * @return HelpFinderUser the user which is created
     */
     @RequestMapping(value = "/updateUserInfo", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateUserInfo(@RequestBody SignupRequest user) {
         // not implementing as part of this project
        return null;
    }
     
     /**
     * get user info of the current logged-in user, only a logged-in user with valid jwt session can access this method
     * @return User 
     */     
    @GetMapping(value = "/getUserInfo")
    @PreAuthorize("hasAuthority('ADD_ADMIN_USER') or hasAuthority('DELETE_USER') or hasAuthority('SEARCH_FOR_WORKERS') or hasAuthority('REVIEW_SITE_FEEDBACK') or hasAuthority('ARCH_SITE_FEEDBACK') or hasAuthority('ADD_ADMIN_USER') or hasAuthority('ALLOWED_TO_BE_HIRE')")   
    public ResponseEntity<?> getUserInfo( ) {     
         SecureUserDetails secUser = UserService.getAutenticatedUser();
         BasicUser user = (BasicUser)userService.findByUsername(secUser.getEmail());
         return user == null ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found") : ResponseEntity.ok(user);        
    }
    
    /***
     * Sends a work inquiry
     * @param workInquiryRequest
     * @return the status message
     */
    
    @RequestMapping(value = "/sendWorkInquiry", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('SEARCH_FOR_WORKERS') ")
    public ResponseEntity<?> sendWorkInquiry(@RequestBody WorkInquiryRequest workInquiryRequest)
    {
        SecureUserDetails secUser = UserService.getAutenticatedUser();
        workInquiryRequest.setHelpFinderUserId(secUser.getId());
        try {
            userService.sendWorkInquiry(workInquiryRequest);
        }
        catch(PermissionDeniedDataAccessException | Error e) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Work Inquiry sent");        
    }
    
    /***
     * gets all work inquiry received by a worker
     * @return List<WorkInquiry>
     */
    @GetMapping(value = "/getReceivedWorkInquiry")
    @PreAuthorize("hasAuthority('ALLOWED_TO_BE_HIRE') or hasAuthority('SEARCH_FOR_WORKERS')")
    public ResponseEntity<?> getReceivedWorkInquiry()
    {
        SecureUserDetails secUser = UserService.getAutenticatedUser();        
        try {
            return ResponseEntity.ok( userService.getWorkInquiryReceivedByUser(secUser.getId(), true));
        }
        catch(PermissionDeniedDataAccessException | Error e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }             
    }
    
    /***
     * commits a work inquiry by a worker
     * @param inquiryId
     * @return status message
     */
    @RequestMapping(value = "/commitInquiry", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('ALLOWED_TO_BE_HIRE')")
    public ResponseEntity<?> commitInquiry(@RequestBody int inquiryId)
    {
        SecureUserDetails secUser = UserService.getAutenticatedUser();        
        try {
            userService.commitInquiry(secUser.getId(), inquiryId);
        }
        catch(PermissionDeniedDataAccessException | Error e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Work Inquiry committed");        
    }
        
    /***
     * hires a works
     * @param inquiryId
     * @return status message
     */
    @RequestMapping(value = "/hireInquiry", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('SEARCH_FOR_WORKERS')")
    public ResponseEntity<?> hireInquiry(@RequestBody int inquiryId)
    {
        SecureUserDetails secUser = UserService.getAutenticatedUser();        
        try {
             userService.hireInquiry(secUser.getId(), inquiryId);
        }
        catch(PermissionDeniedDataAccessException | Error e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Work Inquiry hired");        
    }
    
    /***
     * Cancels an inquiry 
     * @param inquiryId
     * @return return the status
     */
    @RequestMapping(value = "/cancelInquiry", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @PreAuthorize("hasAuthority('SEARCH_FOR_WORKERS')")
    public ResponseEntity<?> cancelInquiry(@RequestBody int inquiryId)
    {
        SecureUserDetails secUser = UserService.getAutenticatedUser();        
        try {
            userService.cancelInquiry(secUser.getId(), inquiryId);
        }
        catch(PermissionDeniedDataAccessException | Error e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.OK).body("Work Inquiry cancelled");        
    }
    
    /***
     * get the inquires sent by a user
     * @return List<WorkInquiries>
     */
    @GetMapping(value = "/getInquiriesSent")
    @PreAuthorize("hasAuthority('SEARCH_FOR_WORKERS')")
    public ResponseEntity<?> getInquiriesSent()
    {
        SecureUserDetails secUser = UserService.getAutenticatedUser();        
        try {
            return ResponseEntity.ok( userService.getWorkInquirySentByUser(secUser.getId(), true));
        }
        catch(PermissionDeniedDataAccessException | Error e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
        
    }
}
