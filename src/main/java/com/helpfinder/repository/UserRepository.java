/*
 
 
  This interface is the implementation for the data access layer to get user related information
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.data.repository.core.RepositoryCreationException;
import org.springframework.stereotype.Component;

import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.request.WorkInquiryRequest;

@Component
//interface to implement functionalities specific to users
public interface UserRepository<T extends User> {
    // get the user for the given userId
    public T getUser(long userId);

    // create a new User and return the User with the new userId
    public T createUser(T user) throws RepositoryCreationException, UserExistException;

    // gets the skills added to a user
    public List<WorkerSkill> getUserSkills(long userId);

    // update the user with the skills
    public boolean updateUserSkills(long userId, List<WorkerSkill> skills);


    // update lat long for a user
    public boolean updateLatLong(long userId, Double[] latLong);

    // update committed status for an inquiry
    public boolean updateCommittedStatusForEnquiry(int inquiryId, boolean committedStatus);

    // update hired status for inquiry
    public boolean updateHiredStatusForEnquiry(int inquiryId, boolean hiredStatus);
    
    //get user by username
    public T findByUsername(String userName);
    
    //get user by emailAddress
    public T findByEmailAddress(String emailAdress);
    
    //set latlong based on the address
    public void updateLatLongByAddress(long userID, String address);
    
    //gets the user based on usertype from the resultset
    public T createUserByTypeFromResultSet(ResultSet result, boolean addSkills) throws SQLException, InvalidAttributesException, ParseException;
    /***
     * creates a user for the given user type  
     */
    public T createUserByType(EUserType userType, EUserType defaultType);
    
    //submit a request
    public void addWorkInquiry(WorkInquiryRequest inquiryRequest) throws RepositoryCreationException;
    //get all inquiries sent to a worker
    public List<WorkInquiry> getWorkInquirySentToWorkerId(long fromHelpUserId, long toWorkUserId, boolean fetchFullUserDetails ) throws RepositoryCreationException;
    public void hireInquiry(long helpFinderUserId, long workInquiryId) throws RepositoryCreationException;
    public void commitInquiry(long workerUserId, long workInquiryId) throws RepositoryCreationException;
    /***
     * Gets the work inquiry for the from helpuser to  another worker,
     */
    public WorkInquiry getWorkInquiryByInquiryId(long workInquiryId, long helpFinderUserId )throws RepositoryCreationException ;
    public void cancelInquiry(long helpFinderUserId, long WorkInquiryId) throws RepositoryCreationException;
    /***
     * Gets the work inquiry received by a user
     * fetchFullUserDetails when set true will return all the user details of worker and the helpfinder user
     */
    public List<WorkInquiry> getWorkInquiryReceivedByUser(long workUserId, boolean fetchFullUserDetails ) throws RepositoryCreationException;

    /***
     * * gets the total work inquires sent by a user to another work user
     * @param helpFinderUserId
     * @param WorkInquiryId
     * @return int the total count
     */
    public int getTotalInquires(long helpFinderUserId, long workUserId) throws RepositoryCreationException;
    /***
     * gets all work inquiry sent by user     * 
     * @param helpFinderUserId
     * @param fetchFullUserDetail 
     * @return
     * @throws RepositoryCreationException
     */
    public List<WorkInquiry> getWorkInquirySentByUser(long helpFinderUserId, boolean fetchFullUserDetail ) throws RepositoryCreationException;
        
    

}
