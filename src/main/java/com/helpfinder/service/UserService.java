/*
 * BU Term project for cs622
 
  This class defines the functionalities required to manage Users
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */

package com.helpfinder.service;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.RegExUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.helpfinder.exception.InvalidAddressException;
import com.helpfinder.exception.InvalidPasswordException;
import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.UserPermissions;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.repository.LocationServiceRepository;
import com.helpfinder.repository.UserRepository;

@Service
public class UserService<T extends BasicUser> {
	
	Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	
	@Autowired
	PasswordEncoder encoder;
	
    @Autowired
    final UserRepository<T> userRepo;
    
    @Autowired
    final LocationServiceRepository locationRepo;
    /***
     * constructor
     * @param userRepo the instance of UserRepository implementation 
     */    
    public UserService(UserRepository<T> userRepo, final LocationServiceRepository locationRepo, PasswordEncoder encoder)    {
        this.userRepo = userRepo;    
        this.locationRepo = locationRepo;
        this.encoder = encoder;
    }
    
    /***
     * gets the users based on the user id param
     * @param userId the userid reference in the system
     * @return User the instance of the user if it exist
     */
    public T getUser(int userId) {
        return userRepo.getUser(userId);
    }
    

   /***
    * 
    * @param <P> type of user to create
    * @param user
    * @return the User object
    * @throws UserExistException
    */
    public <P extends T> T createUser(P user) throws UserExistException {
    	 return userRepo.createUser(user);
    }

    /***
     *  * creates a new user
     * @param address
     * @param firstName
     * @param lastname
     * @param emailAddress
     * @param userName
     * @param password
     * @param userType
     * @return User a type of BasicUser
     * @throws UserExistException
     * @throws InvalidPasswordException 
     */
    public T createUser(String address, String firstName, String lastname, String emailAddress, String userName, String password, EUserType userType) throws UserExistException, InvalidAddressException, InvalidPasswordException {
        //check if user exist
    	if(existsByEmailAddress(emailAddress) != null)
            throw new UserExistException(String.format("User %s already exist", emailAddress));
    	if(!locationRepo.isValidAddress(address))
    		throw new InvalidAddressException(String.format("The given address %s is not valid", address));
    	if(!isValidPassword(password))
    		throw new InvalidPasswordException("Password should between 7 to 20 in length, with one upper case, one lower case char , one special char and one number");
    	//create the correct user type
    	T user =  userRepo.createUserByType(userType, userType);		
		user.setUserInformation(address, firstName, lastname, emailAddress, userType);
	    user.setPassword(encoder.encode(password));
	    user.setUserName(emailAddress);
	    Double[] latLong = locationRepo.getLatLogFromAddress(address);
	    user.setLatLong(latLong);        
	    return userRepo.createUser(user);
        
    }
    
    //method to validate password
    private boolean isValidPassword(String pwd) {
    	//min 7 char, max 20 one upper one lower and one number and on special char
    	Pattern pattern = Pattern.compile(
    	"^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{7,20}$");
    	Matcher match = pattern.matcher(pwd);
    	return match.find();
    }
    /**
     * checks if a user exist if so returns the user using email address 
     * @param emailAddress email address of the user
     * @return User the instance of the user if it exist
     */
    public T existsByEmailAddress(String emailAddress){
        return userRepo.findByEmailAddress(emailAddress);
    }
    
    /***
     * gets the user based on user name 
     * @param userName user name of the user
     * @return User the instance of the user if it exist
     */
    public T findByUsername(String userName) {
        return userRepo.findByUsername(userName);
        
    }
    
    /***
     * creates a new user if the user doesn't exist
     * @param user User instance with all the user details 
     * @return User the instance of the user if it exist
     * @throws UserExistException 
     */
    public T createWorkerUser(T user) throws UserExistException {        
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
    
    /**
     * This will tell if this user is allowed to work
     * A basic user is not allowed to work
     * @return boolean true if allowed to work otherwise false
     */
    public boolean isAllowedToTakeWork() {
    	//check in the authenticated context if user has permissions
    	if(!(authentication instanceof AnonymousAuthenticationToken))
    		return authentication.getAuthorities().contains(new SimpleGrantedAuthority(UserPermissions.ALLOWED_TO_BE_HIRE.name()));
    	return false;
    }
    
    /***
     * checks if user has a certain permissions
     * @param UserPErmission the permission to check for
     * @return boolean true if user has permissions else false
     */    
    public boolean hasPermission(UserPermissions permission) {
       
    	//check in the authenticated context if user has permissions
    	if(!(authentication instanceof AnonymousAuthenticationToken))
    		return authentication.getAuthorities().contains(new SimpleGrantedAuthority(permission.name()));
    	return false;
    }
    
    
    
}
    
