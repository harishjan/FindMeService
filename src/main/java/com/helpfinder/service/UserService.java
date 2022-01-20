package com.helpfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.repository.UserRepository;

@Component
public class UserService {
	final UserRepository userRepo;
	
	@Autowired
	public UserService(UserRepository userRepo)	{
		this. userRepo = userRepo;	
	}
	
	
	public User getUser(int userId) {
		// TODO Auto-generated method stub

		return userRepo.getUser(userId);
	}

	public User createHelpFinderUser(User user) {
		// TODO Auto-generated method stub		
		return userRepo.createUser(user);
		
	}
	
	public User createWorkerUser(User user) {
		// TODO Auto-generated method stub
		user = userRepo.createUser(user);		
		userRepo.updateUserSkills((int)((BasicUser)user).userId, ((WorkerUser)user).getSkills());
		return user;
		
	}
}
