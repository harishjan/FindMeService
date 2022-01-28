/*
 * BU Term project for cs622
 
  This class defines the functionalities required to manage Users
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */

package com.helpfinder.service;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserRole;
import com.helpfinder.model.User;
import com.helpfinder.model.UserPermissions;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.repository.UserRepository;

@Service
public class UserService<T extends User> {
    @Autowired
    final UserRepository<T> userRepo;
    
    /***
     * constructor
     * @param userRepo the instance of UserRepository implementation 
     */    
    public UserService(UserRepository<T> userRepo)    {
        this. userRepo = userRepo;    
    }
    
    /***
     * gets the users based on the user id param
     * @param userId the userid reference in the system
     * @return User the instance of the user if it exist
     */
    public User getUser(int userId) {
        return userRepo.getUser(userId);
    }
    

    /***
     * creates a new user
     * @param user the User instance with all the details for the user
     * @return User the instance of the user if it exist
     * @throws UserExistException 
     */
    public User createUser(T user) throws UserExistException {
        //check if user exist
        if(existsByEmailAddress(user.getEmailAddress()) != null)
            throw new UserExistException(String.format("User %s already exist", user.getEmailAddress()));
        return userRepo.createUser(user);
        
    }
    
    /**
     * checks if a user exist if so returns the user using email address 
     * @param emailAddress email address of the user
     * @return User the instance of the user if it exist
     */
    public User existsByEmailAddress(String emailAddress){
        return userRepo.findByEmailAddress(emailAddress);
    }
    
    /***
     * gets the user based on user name 
     * @param userName user name of the user
     * @return User the instance of the user if it exist
     */
    public User findByUsername(String userName) {
        return userRepo.findByUsername(userName);
        
    }
    
    /***
     * creates a new user if the user doesn't exist
     * @param user User instance with all the user details 
     * @return User the instance of the user if it exist
     * @throws UserExistException 
     */
    public User createWorkerUser(T user) throws UserExistException {        
        //check if user exist
        if(existsByEmailAddress(user.getEmailAddress()) != null)
            throw new UserExistException(String.format("User %s already exist", user.getEmailAddress()));
        // create a new user
        user = userRepo.createUser(user);        
        //update the skills
        userRepo.updateUserSkills((int)((BasicUser)user).getUserId(), ((WorkerUser)user).getSkills());
        return user;
        
    }
    
    
    /**
     * gets the list of inquiries received by  user
     * param userId the id of the user
     * @return List<WorkInquiry> List of Work inquiries
     */
    public List<WorkInquiry> getWorkInquirieReceived(Long userId)    {
        return userRepo.getUserWorkInquiriesReceived(userId);
    }
    
    /**
     * gets all the work inquiries made by the user which are committed by this worker user
     * @param userId the id of the user
     * @return List<WorkInquiry> list of work inquiry which has a committed status as true
     */
    public List<WorkInquiry> getWorkInquiryCommited(Long userId)    {        
        return userRepo.getWorkInquiryCommited(userId);
    }
    
    
    /**
     * gets the list of inquiries where user is hired
     * @param userId the id of the user
     * @return List<WorkInquiry> List of Work inquiries
     */
    public List<WorkInquiry> getWorkInquiriesHired(Long userId)    {    
        return userRepo.getWorkInquiriesHired(userId);        
        
    }
    
    /**
     * gets the list of inquiries sent by a user
     * @param userId the id of the user
     * @return List<WorkInquiry> List of Work inquiries
     */
    public List<WorkInquiry> getWorkInquiriesSent(Long userId)    {    
        return userRepo.getUserWorkInquiriesSent(userId);        
        
    }
    
    
}
    
