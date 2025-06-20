/*
 
 
  This class defines the functionalities required to manage Users
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */

package com.helpfinder.service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.PermissionDeniedDataAccessException;
import org.springframework.data.repository.core.RepositoryCreationException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.helpfinder.common.DateFormatter;
import com.helpfinder.exception.InvalidAddressException;
import com.helpfinder.exception.InvalidPasswordException;
import com.helpfinder.exception.InvalidSkillException;
import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.SecureUserDetails;
import com.helpfinder.model.UserPermissions;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.model.request.SignupRequest;
import com.helpfinder.model.request.WorkInquiryRequest;
import com.helpfinder.repository.LocationServiceRepository;
import com.helpfinder.repository.UserRepository;
import com.helpfinder.repository.WorkerSkillRepository;

@Service
public class UserService<T extends BasicUser> {
       
       Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
       
   @Autowired
   PasswordEncoder encoder;
       
    @Autowired
    final UserRepository<T> userRepo;
    
    @Autowired
    final LocationServiceRepository locationRepo;
    
    @Autowired
    WorkerSkillRepository workSkillRepo;
    
    @Autowired
    private InquiryNotificationService notificationService;

    /***
     * constructor
     * @param userRepo the instance of UserRepository implementation 
     */    
    public UserService(UserRepository<T> userRepo, final LocationServiceRepository locationRepo, 
            PasswordEncoder encoder, WorkerSkillRepository workSkillRepo)    {
        this.userRepo = userRepo;    
        this.locationRepo = locationRepo;
        this.encoder = encoder;
        this.workSkillRepo = workSkillRepo;
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
    public T createUser(SignupRequest signUpRequest, EUserType userType) throws UserExistException, InvalidAddressException, InvalidPasswordException, InvalidSkillException {
        //check if user exist
           if(existsByEmailAddress(signUpRequest.getEmail()) != null)
            throw new UserExistException(String.format("User %s already exist", signUpRequest.getEmail()));
           if(!locationRepo.isValidAddress(signUpRequest.getAddress()))
                  throw new InvalidAddressException(String.format("The given address %s is not valid", signUpRequest.getAddress()));
           if(!isValidPassword(signUpRequest.getPassword()))
                  throw new InvalidPasswordException("Password should between 7 to 20 in length, with one upper case, one lower case char , one special char and one number");
           T user =  userRepo.createUserByType(userType, userType); 
           if(userType.equals(EUserType.ROLE_WORKER_USER)) {
               if(signUpRequest.getSkills() == null || signUpRequest.getSkills().size() == 0)
                   throw new InvalidSkillException("Please add skills that are relevant for you");
               // sanitize the skills
               user.setSkills(sanitizeSkills(signUpRequest.getSkills()));              
           }
           //create the correct user type
                        
              user.setUserInformation(signUpRequest.getAddress(), signUpRequest.getFirstName(), signUpRequest.getLastName(), signUpRequest.getEmail(), userType);
           user.setPassword(encoder.encode(signUpRequest.getPassword()));       
           user.setUserName(signUpRequest.getEmail());
           user.setUserDescription(signUpRequest.getUserDescription());
           double[] latLong = locationRepo.getLatLogFromAddress(signUpRequest.getAddress());
           user.setLatLong(latLong);        
           return userRepo.createUser(user);
        
    }
    

    /***     * 
     *check the skills are valid entries and returns the list skill instances
     * 
     * @param skills the list of workerskills to sanitize
     * @return List<WorkSkilll> the sanitized skills
     * @throws InvalidSkillException
     */
    private List<WorkerSkill> sanitizeSkills(List<WorkerSkill> skills) throws InvalidSkillException {
        List<WorkerSkill> sanitizedSkills = new ArrayList<>(); 
      for(WorkerSkill skill: skills) {
            WorkerSkill workSkill =  this.workSkillRepo.getWorkerskillByName(skill.getSkillName());
            if(workSkill == null)
                throw new InvalidSkillException(String.format("Invalid skill found %s ", skill.getSkillName()));            
            sanitizedSkills.add(workSkill);            
        }
        return sanitizedSkills;
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
    
    //sends a new work inquiry
    /***
     * 
     * @param inquiryRequest
     * @throws PermissionDeniedDataAccessException
     * @throws Error
     */
    public void sendWorkInquiry(WorkInquiryRequest inquiryRequest) throws PermissionDeniedDataAccessException, Error {
        
        if(getAutenticatedUserId() != inquiryRequest.getHelpFinderUserId() || getAutenticatedUserId() == inquiryRequest.getWorkerUserId())
            throw new PermissionDeniedDataAccessException("This operation is not allowed", null);
        try
        {
            // checking if there is already an inquiry 
            List<WorkInquiry> inquiries = userRepo.getWorkInquirySentToWorkerId(inquiryRequest.getHelpFinderUserId(), inquiryRequest.getWorkerUserId(), false);
            if(inquiries != null && inquiries.size() > 0) {
                // checking if there is already an inquiry 
                if(inquiries.stream().filter(x -> x.getIsCancelled() == false && (DateFormatter.compareDatePart(x.getWorkEndDate(), DateFormatter.getDateNow()) >=0)).count() > 0) {
                    throw new Error("There is already a work inquiry sent to this user, cancel the existing inquire if not required");
                }
               
            }
            //dates should be valid
            if(DateFormatter.compareDatePart(inquiryRequest.getWorkEndDate(), DateFormatter.getDateNow()) <=0 || 
                    DateFormatter.compareDatePart(inquiryRequest.getWorkStartDate() ,DateFormatter.getDateNow()) <= 0 ||
                    DateFormatter.compareDatePart(inquiryRequest.getWorkStartDate() ,inquiryRequest.getWorkEndDate()) > 0
                    ) {
                throw new Error("The date for hiring should be in the future and the hiring start date cannot be after hiring end date");
            }
            userRepo.addWorkInquiry(inquiryRequest);
        }catch(RepositoryCreationException ex){
            throw new Error("Error processing the request");
        }
    }
    
    //get all inquiries sent to a worker
    //fetchFullUserDetails if passed true will also return all the user details of helpfiner user and worker user
    /***
     * 
     * @param fromHelpFinderUserId
     * @param toWorkUserId
     * @param fetchFullUserDetails
     * @return
     * @throws PermissionDeniedDataAccessException
     * @throws Error
     */
    public List<WorkInquiry> getWorkInquirySentToWorkerId(long fromHelpFinderUserId, long toWorkUserId, boolean fetchFullUserDetails ) throws PermissionDeniedDataAccessException, Error  {
        if(getAutenticatedUserId() != fromHelpFinderUserId)
            throw new PermissionDeniedDataAccessException("This operation is not allowed", null);
        try
        {
            List<WorkInquiry> inquiries = userRepo.getWorkInquirySentToWorkerId(fromHelpFinderUserId, toWorkUserId, fetchFullUserDetails);         
            //inquiry dates should be valid
            if(inquiries != null && inquiries.size() > 0) {
                inquiries = inquiries.stream().filter(x -> x.getWorkEndDate().before(DateFormatter.getDateNow()) || 
                        x.getWorkStartDate().before(DateFormatter.getDateNow())).collect(Collectors.toList());
            }
            return inquiries;
        }catch(RepositoryCreationException ex){
            throw new Error("Error processing the request");
        }
        
    }
    
    //hire Inquiry submitted by a helpfinderuser to worker user with a specific workinquiryid
    /***
     *  
     * @param helpFinderUserId
     * @param WorkInquiryId
     * @throws PermissionDeniedDataAccessException
     * @throws Error
     */
    public void hireInquiry(long helpFinderUserId, long WorkInquiryId) throws PermissionDeniedDataAccessException, Error  {
        if(getAutenticatedUserId() != helpFinderUserId)
            throw new PermissionDeniedDataAccessException("This operation is not allowed", null);
        try
        {
            userRepo.hireInquiry(helpFinderUserId, WorkInquiryId);
        }catch(RepositoryCreationException ex){
            throw new Error("Error processing the request");
        }
        
    }
    
    //commits an Inquiry submitted by a helpfinderuser to worker user with a specific workinquiryid
    /***
     * 
     * @param workerUserId     * 
     * @param WorkInquiryId
     * @throws PermissionDeniedDataAccessException
     * @throws Error
     */
    public void commitInquiry(long workerUserId, long WorkInquiryId) throws PermissionDeniedDataAccessException, Error  {
        if(getAutenticatedUserId() != workerUserId)
            throw new PermissionDeniedDataAccessException("This operation is not allowed", null);
        try
        {
            userRepo.commitInquiry(workerUserId, WorkInquiryId);
        }catch(RepositoryCreationException ex){
            throw new Error("Error processing the request");
        }
    }
    
    //gets the WorkInquiry for the given inquiryId and submitted by helpuser
    /***
     * 
     * @param workInquiryId
     * @param helpFinderUserId
     * @return
     * @throws PermissionDeniedDataAccessException
     * @throws Error
     */
    public WorkInquiry getWorkInquiryByInquiryId(long workInquiryId, long helpFinderUserId ) throws PermissionDeniedDataAccessException, Error  {
        if(getAutenticatedUserId() != helpFinderUserId)
            throw new PermissionDeniedDataAccessException("This operation is not allowed", null);
        try
        {
            return userRepo.getWorkInquiryByInquiryId(workInquiryId, helpFinderUserId );
        }catch(RepositoryCreationException ex){
            throw new Error("Error processing the request");
        }
    }
    
    /***
     * This method updates the status of a inquiry to cancelled     * 
     * @param helpFinderUserId
     * @param WorkInquiryId
     * @throws RepositoryCreationException
     */
    public void cancelInquiry(long helpFinderUserId, long workInquiryId) throws PermissionDeniedDataAccessException, Error{
        if(getAutenticatedUserId() != helpFinderUserId)
            throw new PermissionDeniedDataAccessException("This operation is not allowed", null);
        try
        {
            userRepo.cancelInquiry(helpFinderUserId, workInquiryId);
        }catch(RepositoryCreationException ex){
            throw new Error("Error processing the request");
        }
    }
    
    
    /***
     * gets the work inquiry received by a user
     * @param workUserId
     * @param fetchFullUserDetails
     * @return
     * @throws PermissionDeniedDataAccessException
     * @throws Error
     */
    public List<WorkInquiry> getWorkInquiryReceivedByUser(long workUserId, boolean fetchFullUserDetails ) throws PermissionDeniedDataAccessException, Error{
        if(getAutenticatedUserId() != workUserId)
            throw new PermissionDeniedDataAccessException("This operation is not allowed", null);
        try
        {
            List<WorkInquiry> inquiries = userRepo.getWorkInquiryReceivedByUser(workUserId, fetchFullUserDetails);
            //inquiry dates should be valid
            if(inquiries != null && inquiries.size() > 0) {
                inquiries = inquiries.stream().filter(x -> x.getWorkEndDate().after(DateFormatter.getDateNow()) || 
                        x.getWorkStartDate().after(DateFormatter.getDateNow())).collect(Collectors.toList());
            }
            return inquiries;
        }catch(RepositoryCreationException ex){
            throw new Error("Error processing the request");
        }
    }
    
    
    /***
     * gets the work inquiry sent by a user
     * @param workUserId
     * @param fetchFullUserDetails
     * @return
     * @throws PermissionDeniedDataAccessException
     * @throws Error
     */
    public List<WorkInquiry> getWorkInquirySentByUser(long helpFinderUserId, boolean fetchFullUserDetails ) throws PermissionDeniedDataAccessException, Error{
        if(getAutenticatedUserId() != helpFinderUserId)
            throw new PermissionDeniedDataAccessException("This operation is not allowed", null);
        try
        {
            List<WorkInquiry> inquiries = userRepo.getWorkInquirySentByUser(helpFinderUserId, fetchFullUserDetails);
            //inquiry dates should be valid
            if(inquiries != null && inquiries.size() > 0) {
                inquiries = inquiries.stream().filter(x -> x.getWorkEndDate().after(DateFormatter.getDateNow()) || 
                        x.getWorkStartDate().after(DateFormatter.getDateNow())).collect(Collectors.toList());
            }
            return inquiries;
        }catch(RepositoryCreationException ex){
            throw new Error("Error processing the request");
        }
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
    
    
    
    //get user details from the current session
    public static String getAutenticatedUserEmail() {
        return getAutenticatedUser().getEmail();   
    }
    public static long getAutenticatedUserId() {
        return getAutenticatedUser().getId();   
    }
    public static SecureUserDetails getAutenticatedUser() {
        return (SecureUserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    public boolean userHasPermission(UserPermissions permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      //check in the authenticated context if user has permissions
        if(!(authentication instanceof AnonymousAuthenticationToken))
               return authentication.getAuthorities().contains(new SimpleGrantedAuthority(permission.name()));
        return false;
    }
    
}
    
