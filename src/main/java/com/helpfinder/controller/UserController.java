package com.helpfinder.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.repository.UserRepository;
import com.helpfinder.service.UserService;
@RestController
@RequestMapping(value = "/user")
// not yet implemented
public class UserController {
	
	@Autowired
	UserService userService;
	
	
	/**
	 * creates a new Worker user
	 * @return WorkerUser the user which is created
	 */
	@RequestMapping(value = "/createWorkerUser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json" )
	public WorkerUser createWorkerUser(@RequestBody WorkerUser user) {
		return null;
	}
	
	/**
	 * creates a new HelpFinder
	 * @return HelpFinderUser the user which is created
	 */
	 @RequestMapping(value = "/createHelpFinderUser", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
	public HelpFinderUser createHelpFinderUser(@RequestBody HelpFinderUser user) {
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


}
