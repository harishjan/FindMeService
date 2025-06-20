/*
 
 
  This class implements the sqlite data access layer for access user related information
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;



import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.RepositoryCreationException;
import org.springframework.stereotype.Component;

import com.helpfinder.common.DateFormatter;
import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.AdminUser;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.ModeratorUser;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.model.request.WorkInquiryRequest;

@Component
// implementation is still not completed
public class SQLiteUserRepository<T extends BasicUser> implements UserRepository<T> {
        
    // stores the location service repo
    @Autowired
    LocationServiceRepository locationServiceRepo;
    @Autowired
    WorkerSkillRepository workerSkillRepository;

    @Autowired
    DatabaseRepository databaseRepository;
    String selectUserByIdQuery = "select userId, address, lat, long, firstName, lastName, emailAddress, userName, "
            + "userType, insertedOn, updatedOn, userDescription from user where userID = ?;";    
    
    String insertUserQuery = "insert into user (address, lat, long, firstName, lastName, emailAddress, userName, "
            + "password, userType, cosLatRadians, sinLatRadians, cosLongRadians, sinLongRadians, insertedOn, userDescription) "
            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    
    String insertSkill = "insert into UserWorkSkill (workerSkillId, userId) values (?, ?);";
    String selectUserQuery = "select userId, address, lat, long, firstName, lastName, emailAddress, userName, "
            + "password, userType, insertedOn, updatedOn, userDescription from user where emailaddress = ?;";
    String selectUserWorkSkillsQuery = "select userWorkerSkillId, workerSkillId  from UserWorkSkill where userId = ?;";
    
    // constructor added to create dummy data
    public SQLiteUserRepository(LocationServiceRepository locationServiceRepo, WorkerSkillRepository workerSkillRepository
            , DatabaseRepository databaseRepository) {
        this.locationServiceRepo = locationServiceRepo;
        this.workerSkillRepository = workerSkillRepository;
        this.databaseRepository = databaseRepository;
    
    }

    @Override
    public T getUser(long userId) {
        try
        {
            
            return databaseRepository.executeSelectQuery(selectUserByIdQuery, (statement)->{
                try {
                       //set the parameters in the sql query
                        statement.setLong(1, userId);  
                    }
                    catch(SQLException ex)
                    {
                        throw new RepositoryCreationException("Error getting user "+ ex.getMessage(), SQLiteUserRepository.class);
                    }    
                },
                (result) ->{
                             
                    try {
                        return createUserByTypeFromResultSet(result, true);
                    } catch (InvalidAttributesException | SQLException | ParseException e) {
                        System.err.println("Error fetching user by id");
                        return null;
                    }
                });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting user "+ ex.getMessage(), SQLiteUserRepository.class);           
        }
    }
    
    
    /***
     * This method creates a new user and stores it in sqlite
     * @param user is the instance of User object
     * @return user the user instance after setting the latlong and userId
     */

    @Override
    public T createUser(T user) throws RepositoryCreationException, UserExistException {
        
           if(getByEmail(user.getEmailAddress()) != null)
                  throw new UserExistException("User Already Exist");
           // saving the data should happen as a transaction as both user details and skills should be saved
           Connection connection = null;
           
        try
        {       
               connection = databaseRepository.getConnection();
               
               // create a user and get the userId
            int userId = saveUserDetails(user, connection);
            System.out.println(userId);
            //if user ID is not created, roll back and end the flow
            if(userId == -1) {
                   if(connection != null)
                          connection.rollback();
                throw new RepositoryCreationException("Error creating user ", SQLiteUserRepository.class);
            }
            // create skills if exist
            if(user.getSkills() != null && user.getSkills().size() > 0)
                   addSkillls(userId, user.getSkills(), connection);
            
            connection.commit();            
            user.setUserId(userId);
           } 
        catch(ClassNotFoundException | SQLException | RepositoryCreationException ex)
        {       
               //rollback everything if there an exception 
               if(connection != null) {
                            try {
                                   connection.rollback();
                            } catch (SQLException e) {                                   
                                   e.printStackTrace();
                            }
               }
            throw new RepositoryCreationException("Error creating user "+ ex.getMessage(), SQLiteUserRepository.class);
        }    
        finally
        {               
             if (connection != null) {
                            try {
                                   connection.close();
                            } catch (SQLException e) {                                   
                                   e.printStackTrace();
                            }
             }
        }
        return user;
    }
    
    private int saveUserDetails(T user, Connection connection) throws SQLException
    {
            return databaseRepository.executeInsertQuery(insertUserQuery, connection, (statement)->{
             try {
             
                     statement.setString(1, user.getAddress());                
                     statement.setFloat(2, (float) (user.getLatLong().length < 2 
                                   || user.getLatLong()[0] ==  0.0d ? -1.0 : (float)user.getLatLong()[0]));
                     statement.setFloat(3, (float) (user.getLatLong().length < 2  
                                   || user.getLatLong()[1] ==  0.0d ? -1.0 : (float)user.getLatLong()[1]));
                     statement.setString(4, user.getFirstName());
                     statement.setString(5, user.getLastName());
                     statement.setString(6, user.getEmailAddress());
                     statement.setString(7, user.getEmailAddress());
                     statement.setString(8, user.getPassword());
                     statement.setString(9, user.getUserType().name());                        
                     statement.setFloat(10, (float) (user.getLatLong().length < 2 
                                   || user.getLatLong()[0] ==  0.0d ? -1.0 :  WorkForceLocatorRepository.getCosRadians(user.getLatLong()[0])));
                     statement.setFloat(11, (float) (user.getLatLong().length < 2 
                                   || user.getLatLong()[0] ==  0.0d ? -1.0 : WorkForceLocatorRepository.getSinRadians(user.getLatLong()[0])));
                     statement.setFloat(12, (float) (user.getLatLong().length < 2  
                                   || user.getLatLong()[1] ==  0.0d ? -1.0 : WorkForceLocatorRepository.getCosRadians(user.getLatLong()[1])));                        
                     statement.setFloat(13, (float) (user.getLatLong().length < 2  
                                   || user.getLatLong()[1] ==  0.0d ? -1.0 : WorkForceLocatorRepository.getSinRadians(user.getLatLong()[1])));
                     statement.setString(14, DateFormatter.convertToSystemDateNowString());
                     statement.setString(15, user.getUserDescription());
                     
                 }
                 catch(SQLException ex) {
                     throw new RepositoryCreationException("Error creating user "+ ex.getMessage(), SQLiteUserRepository.class);
                
                 }    
                });
    }
    
    //saves the list of skill, with an existing user 
    private void addSkillls(long userId, List<WorkerSkill> skills, Connection connection) throws SQLException {
           for(WorkerSkill skill : skills) {
                  databaseRepository.executeInsertQuery(insertSkill, connection, (statement)->{
                   try {
                   
                           statement.setInt(1, skill.getWorkSkillId());             
                           statement.setLong(2, userId);                    
                       }
                       catch(SQLException ex) {
                           throw new RepositoryCreationException("Error creating workskills "+ ex.getMessage(), SQLiteUserRepository.class);
                      
                       }    
                      });
           }
    }
    
    //get the skills of a user
    public List<WorkerSkill> getUserSkills(long userId)
    {
        try
        {
            
            return (List<WorkerSkill>)databaseRepository.executeSelectQuery(selectUserWorkSkillsQuery, (statement)->{
                try {
                       //set the parameters in the sql query
                              statement.setLong(1, userId);  
                    }
                    catch(SQLException ex)
                    {
                        throw new RepositoryCreationException("Error getting user "+ ex.getMessage(), SQLiteUserRepository.class);
                    }    
                },
                (result) ->{
                             
                      List<WorkerSkill> skills  = new ArrayList<WorkerSkill>();
                       try {
                           //only interested in the first record
                           while(result != null && result.next()) {
                               skills.add(workerSkillRepository.getWorkerskillById(result.getInt("workerSkillId")));                                                
                           }
                       } catch (SQLException ex) {
                              throw new RepositoryCreationException("Error fetching result set  "+ ex.getMessage(), SQLiteUserRepository.class);
                       }
                       return skills;
                });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting user "+ ex.getMessage(), SQLiteUserRepository.class);           
        }
           
    }
    /***
     * creates a user for the given user type
     * @param userType
     * @param defaultType if usertype is not found what is the default type to create, this can be null as well
     * @return User
     */
    public T createUserByType(EUserType userType, EUserType defaultType) {
           
           T user  = null;           
           //create the correct user type
              switch(userType) {
                  case ROLE_ADMIN:
                         user = (T)new AdminUser();
                         break;
                  case ROLE_HELPFINDER_USER:
                         user = (T)new HelpFinderUser();
                         break;
                  case ROLE_MODERATOR:
                         user = (T)new ModeratorUser();
                         break;
                  case ROLE_WORKER_USER:
                         user = (T)new WorkerUser();
                         break;
                  default: 
                         if(defaultType != null)
                                user = createUserByType(userType, null);
              }       
                     
              return user;
    }
    
    /**
     * This method constructs the user object based on the user typetype
     * @param result
     * @return the user based on the user type
     * @throws SQLException
     * @throws InvalidAttributesException
     * @throws ParseException
     */
    public T createUserByTypeFromResultSet(ResultSet result, boolean getSkills) throws SQLException, InvalidAttributesException, ParseException {
           
           //to check if columns exist
           ResultSetMetaData metadata = result.getMetaData();
           //get the user by type
           T user  = createUserByType(EUserType.forName(result.getString("userType")).get(), null);
           if(user == null)
                     throw new InvalidAttributesException("userType column not found");
           //number of columns
           int columns = metadata.getColumnCount();
              
              //to store latlong
           double[] latLong = new double[2];
           // now set each column which is available in the result set, if a column doesn't exist, then that field in user object is not set
           for (int x = 1; x <= columns; x++) {
               switch(metadata.getColumnName(x)) {               
                      case "userId":
                             user.setUserId(result.getInt("userId"));
                             break;
                      case "address":
                             user.setAddress(result.getString("address"));
                             break;
                      case "lat":
                      //-1.0 is the default value set if lat log is not available               
                             latLong[0] = result.getFloat("lat") == -1.0 ? null : (double)result.getDouble("lat");
                             break;
                      case "long":                      
                             latLong[1] = result.getFloat("long") == -1.0 ? null : (double)result.getDouble("long");
                             break;               
                      case "firstName":
                             user.setFirstName(result.getString("firstName"));
                             break;
                      case "lastName":
                             user.setLastName(result.getString("lastName"));
                             break;
                      case "emailAddress":
                             user.setEmailAddress(result.getString("emailAddress"));
                             user.setUserName(result.getString("emailAddress"));
                             break;
                      case "password":
                             user.setPassword(result.getString("password"));
                             break;
                      case "userType":
                             user.setUserType(EUserType.forName(result.getString("userType")).get());                             
                             break;
                      case "cosLatRadians":
                             user.setCosLatRadians(result.getFloat("cosLatRadians") == -1.0 ? null : (double)result.getDouble("cosLatRadians"));
                             break;
                      case "sinLatRadians":
                             user.setSinLatRadians(result.getFloat("sinLatRadians") == -1.0 ? null : (double)result.getDouble("sinLatRadians"));
                             break;
                      case "cosLongRadians":
                             user.setCosLongRadians(result.getFloat("cosLongRadians") == -1.0 ? null : (double)result.getDouble("cosLongRadians"));
                             break;
                      case "sinLongRadians":
                             user.setSinLongRadians(result.getFloat("sinLongRadians") == -1.0 ? null : (double)result.getDouble("sinLongRadians"));
                             break;
                      case "insertedOn":
                             user.setCreatedOn(DateFormatter.convertToSystemDate(result.getString("insertedOn")));
                             break;
                      case "updatedOn":
                             user.setUpdatedOn(result.getString("updatedOn") == null ? null : DateFormatter.convertToSystemDate(result.getString("updatedOn")));
                             break;
                      case "userDescription":
                          user.setUserDescription(result.getString("userDescription"));
                       default:
                              break;
               }
               
           }
           //store latlong
           user.setLatLong(latLong);
           user.setSkills(getUserSkills(user.getUserId()));
           return user;
              
    }
    
    /***
     * Get user details by email, system stores email and username as same, 
     * hence this is is a common function to retrieve user using both
     * @param emailAddress
     * @return User
     */

    private T getByEmail(String emailAddress) {           
           
           try
        {
            return databaseRepository.executeSelectQuery(selectUserQuery, (statement)->{
                try {
                       //set the parameters in the sql query
                              statement.setString(1, emailAddress);  
                    }
                    catch(SQLException ex)
                    {
                        throw new RepositoryCreationException("Error getting user "+ ex.getMessage(), SQLiteUserRepository.class);
                    }    
                },
                      (result) ->{
                             
                             T user  = null;
                       try {
                                        //only interested in the first record
                                           if(result != null && result.next()) {
                                                  user = createUserByTypeFromResultSet(result, true);
                                           }
                       } catch (SQLException | InvalidAttributesException | ParseException ex) {
                              throw new RepositoryCreationException("Error fetching result set  "+ ex.getMessage(), SQLiteUserRepository.class);
                                   }
                       return user;
                      });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting user "+ ex.getMessage(), SQLiteUserRepository.class);           
        }
           
    }
    /***
     * get user based on userName
     * @param userName user name of the user
     * @return User the matching user with userName
     */
    @Override
    public T findByUsername(String userName)
    {
        return getByEmail(userName);
    }
    
    //get user by emailAddress
    /***
     * get user based on email address
     * @param emailAddress email address of the user
     * @return User the matching user with email address
     */
    @Override
    public T findByEmailAddress(String emailAdress)
    {
           return getByEmail(emailAdress);
    }
    
    @Override
    public void updateLatLongByAddress(long userID, String address) {
        // TODO Auto-generated method stub
        
    }

 

   

    @Override
    public boolean updateUserSkills(long userId, List<WorkerSkill> skills) {
        // TODO Auto-generated method stub
        return false;
    }
  

    @Override
    public boolean updateLatLong(long userId, Double[] latLong) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateCommittedStatusForEnquiry(int inquiryId, boolean committedStatus) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean updateHiredStatusForEnquiry(int inquiryId, boolean hiredStatus) {
        // TODO Auto-generated method stub
        return false;
    }
    
    
  

    private String commitInquiryQuery = "UPDATE WorkInquiry SET committedDate = ?, updatedOn = ?, isCommitted = 1 where workerUserId = ? and InquiryId = ?;";
    private String hireInquiryQuery = "UPDATE WorkInquiry SET hiredDate = ?, updatedOn = ?, isHired = 1 where helpFinderUserId = ? and InquiryId = ?;";
    private String addInquiryQuery = "INSERT into WorkInquiry (workDescription, workStartDate, workEndDate,  insertedOn, helpFinderUserId, "
            + "workerUserId, distanceFoundAwayfrom ) VALUES(?,?,?,?,?,?,?);";
    private String selectInquirySentFromUserToWorker = "SELECT inquiryId,  isCommitted,    committedDate,  workStartDate,  workEndDate,    helpFinderUserId,   "
            + "workerUserId,   hiredDate,  isHired,    workDescription,    distanceFoundAwayfrom,  insertedOn, updatedOn, cancelledDate, iscancelled from "
            + "WorkInquiry where workerUserId = ? and helpFinderUserId = ? order by inquiryId limit 100;"; 
    private String selectInquiryWithInquiryId = "SELECT inquiryId,  isCommitted,    committedDate,  workStartDate,  workEndDate,    helpFinderUserId,   workerUserId,   "
            + "hiredDate,  isHired,    workDescription,    distanceFoundAwayfrom,  insertedOn, updatedOn, cancelledDate, iscancelled from WorkInquiry where WorkInquiryId = ? "
            + "and helpFinderUserId = ? order by inquiryId limit 100;";
    private String selectTotalInquiries = "SELECT helpFinderUserId, workerUserId, count(*) as totalCount from WorkInquiry WHERE helpFinderUserId = ? and workerUserId = ? "
            + "group by helpFinderUserId, workerUserId;";
    private String selectInquiryReceivedByUser = "SELECT inquiryId,  isCommitted,    committedDate,  workStartDate,  workEndDate,    helpFinderUserId,   workerUserId,   "
            + "hiredDate,  isHired,    workDescription,    distanceFoundAwayfrom,  insertedOn, updatedOn, committedDate,  cancelledDate, iscancelled from WorkInquiry "
            + "where workerUserId = ? order by inquiryId limit 100;";
    private String selectInquirySentByUser = "SELECT inquiryId,  isCommitted,    committedDate,  workStartDate,  workEndDate,    helpFinderUserId,   workerUserId,   hiredDate,  "
            + "isHired,    workDescription,    distanceFoundAwayfrom,  insertedOn, updatedOn, cancelledDate, iscancelled from WorkInquiry where helpFinderUserId = ? order by inquiryId limit 100;";
    private String cancelInquiryQuery = "UPDATE WorkInquiry SET cancelledDate = ?, updatedOn = ?, iscancelled = 1 where helpFinderUserId = ? and InquiryId = ?;";
    
    
    
    /***
     * gets all work inquiry sent by user
     * @param workInquiryId
     * @param helpFinderUserId
     * @return
     * @throws RepositoryCreationException
     */
    public List<WorkInquiry> getWorkInquirySentByUser(long helpFinderUserId, boolean fetchFullUserDetail )
            throws RepositoryCreationException {
        
        try
        {
            
            return (List<WorkInquiry>)databaseRepository.executeSelectQuery(selectInquirySentByUser, (statement)->{
                try {
                       //set the parameters in the sql query
                      statement.setLong(1, helpFinderUserId);                            
                    }
                    catch(SQLException ex)
                    {
                        throw new RepositoryCreationException("Error getting inquiry "+ ex.getMessage(), SQLiteUserRepository.class);
                    }    
                },
                (result) ->{
                             
                      List<WorkInquiry> inquiries  = new ArrayList<WorkInquiry>();
                       try {
                           while(result != null && result.next()) {                            
                                inquiries.add(createWorkInquiryFromResultSet(result, fetchFullUserDetail));
                                                                            
                           }
                       } catch (SQLException | ParseException ex) {
                              throw new RepositoryCreationException("Error fetching result set  "+ ex.getMessage(), SQLiteUserRepository.class);
                       }
                       return inquiries;
                });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting inquiry "+ ex.getMessage(), SQLiteUserRepository.class);           
        }
        
    }
    
    /***
     * gets the work inquiry based on the inquiry id and the user who has sent the inquiry
     * @param workInquiryId
     * @param helpFinderUserId
     * @return
     * @throws RepositoryCreationException
     */
    public WorkInquiry getWorkInquiryByInquiryId(long workInquiryId, long helpFinderUserId )
            throws RepositoryCreationException {
        
        try
        {
            
            return (WorkInquiry)databaseRepository.executeSelectQuery(selectInquiryWithInquiryId, (statement)->{
                try {
                       //set the parameters in the sql query
                      statement.setLong(1, workInquiryId);
                      statement.setLong(2, helpFinderUserId);  
                    }
                    catch(SQLException ex)
                    {
                        throw new RepositoryCreationException("Error getting inquiry "+ ex.getMessage(), SQLiteUserRepository.class);
                    }    
                },
                (result) ->{
                             
                      WorkInquiry inquiries  = null;
                       try {
                           //only interested in the first record
                           if(result != null && result.next()) {                            
                               inquiries = createWorkInquiryFromResultSet(result, true);
                                                                            
                           }
                       } catch (SQLException | ParseException ex) {
                              throw new RepositoryCreationException("Error fetching result set  "+ ex.getMessage(), SQLiteUserRepository.class);
                       }
                       return inquiries;
                });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting inquiry "+ ex.getMessage(), SQLiteUserRepository.class);           
        }
        
    }
    
    /***
     * This method updates the status of a inquiry to hired     * 
     * @param helpFinderUserId
     * @param WorkInquiryId
     * @throws RepositoryCreationException
     */
    public void hireInquiry(long helpFinderUserId, long WorkInquiryId) throws RepositoryCreationException{
        try {
            int result = databaseRepository.executeUpdateQuery(hireInquiryQuery, null, (statement)->{
             try {
             
                     statement.setString(1, DateFormatter.convertToSystemDateNowString());                
                     statement.setString(2, DateFormatter.convertToSystemDateNowString());                        
                     statement.setLong(3, helpFinderUserId);
                     statement.setLong(4, WorkInquiryId);
                                                       
                 }
                 catch(SQLException ex) {
                     throw new RepositoryCreationException("Error updating work inquiry "+ ex.getMessage(), SQLiteUserRepository.class);
                
                 }    
                });
            
            if(result == -1)
                throw new RepositoryCreationException("Error updating work inquiry  ", getClass());
            //else:: to do
            //    notificationService.notifyUserAboutWork(null);
        } catch (SQLException e) {
            throw new RepositoryCreationException("Error updating work inquiry  ", getClass());
        }
    }
    
    /***
     * This method updates the status of a inquiry to cancelled     
     * @param helpFinderUserId
     * @param WorkInquiryId
     * @throws RepositoryCreationException
     */
    public void cancelInquiry(long helpFinderUserId, long WorkInquiryId) throws RepositoryCreationException{
        try {
            int result = databaseRepository.executeUpdateQuery(cancelInquiryQuery, null, (statement)->{
             try {
             
                     statement.setString(1, DateFormatter.convertToSystemDateNowString());                
                     statement.setString(2, DateFormatter.convertToSystemDateNowString());                                     
                     statement.setLong(3, helpFinderUserId);
                     statement.setLong(4, WorkInquiryId);
                                                       
                 }
                 catch(SQLException ex) {
                     throw new RepositoryCreationException("Error updating work inquiry "+ ex.getMessage(), SQLiteUserRepository.class);
                
                 }    
                });
            
            if(result == -1)
                throw new RepositoryCreationException("Error updating work inquiry  ", getClass());
            //else:: to do
            //    notificationService.notifyUserAboutWork(null);
        } catch (SQLException e) {
            throw new RepositoryCreationException("Error updating work inquiry  ", getClass());
        }
    }
    /***
     * this method updates the status of a inquiry as commited from worker
     * @param workerUserId     * 
     * @param WorkInquiryId
     * @throws RepositoryCreationException
     */
    public void commitInquiry(long workerUserId, long WorkInquiryId) throws RepositoryCreationException{
        try {
            int result = databaseRepository.executeUpdateQuery(commitInquiryQuery, null, (statement)->{
             try {
             
                     statement.setString(1, DateFormatter.convertToSystemDateNowString());                
                     statement.setString(2, DateFormatter.convertToSystemDateNowString());
                     statement.setLong(3, workerUserId);  
                     statement.setLong(4, WorkInquiryId);
                                                       
                 }
                 catch(SQLException ex) {
                     throw new RepositoryCreationException("Error updating work inquiry "+ ex.getMessage(), SQLiteUserRepository.class);
                
                 }    
                });
            
            if(result == -1)
                throw new RepositoryCreationException("Error updating work inquiry  ", getClass());
            //else:: to do
            //    notificationService.notifyUserAboutWork(null);
        } catch (SQLException e) {
            throw new RepositoryCreationException("Error updating work inquiry  ", getClass());
        }
    }
    
    /***
     * This method submits a work inquiry
     */
    public void addWorkInquiry(WorkInquiryRequest inquiryRequest) throws RepositoryCreationException{

        try {
            int result = databaseRepository.executeInsertQuery(addInquiryQuery, null, (statement)->{
             try {
             
                     statement.setString(1, inquiryRequest.getWorkDescription());                
                     statement.setString(2, DateFormatter.convertToSystemDateString(inquiryRequest.getWorkStartDate()));
                     statement.setString(3, DateFormatter.convertToSystemDateString(inquiryRequest.getWorkEndDate()));
                     statement.setString(4, DateFormatter.convertToSystemDateNowString());
                     statement.setLong(5, inquiryRequest.getHelpFinderUserId());
                     statement.setLong(6, inquiryRequest.getWorkerUserId());
                     statement.setDouble(7, inquiryRequest.getDistanceFoundAwayfrom());       
                 }
                 catch(SQLException ex) {
                     throw new RepositoryCreationException("Error while sending the request "+ ex.getMessage(), SQLiteUserRepository.class);
                
                 }    
                });
            
            if(result == -1)
                throw new RepositoryCreationException("Error while sending the request", getClass());
          
        } catch (SQLException e) {
            throw new RepositoryCreationException("Error while sending the request", getClass());
        }
    }
    
    //creates a work skillset from result set
    private WorkInquiry createWorkInquiryFromResultSet(ResultSet result, boolean fetchFullUserDetails) throws SQLException, ParseException {
      //to check if columns exist
        ResultSetMetaData metadata = result.getMetaData();
        WorkInquiry inquiry = new WorkInquiry();        
        //number of columns
        int columns = metadata.getColumnCount();           
        // now set each column which is available in the result set, if a column doesn't exist, then that field in user object is not set
        for (int x = 1; x <= columns; x++) {
            switch(metadata.getColumnName(x)) {
            case "inquiryId"  :
                inquiry.setInquiryId(result.getInt("inquiryId"));
                break;
            case "isCommitted":
                inquiry.setIsCommitted(result.getInt("isCommitted") == 1 ? true : false);
                break;
            case "committedDate":
                inquiry.setCommitedon(result.getString("committedDate") == null ? null : DateFormatter.convertToSystemDate(result.getString("committedDate")));
                break;
            case "iscancelled":
                inquiry.setIsCancelled(result.getInt("iscancelled") == 1 ? true : false);
                break;
            case "cancelledDate":
                inquiry.setCancelledDate(result.getString("cancelledDate") == null ? null : DateFormatter.convertToSystemDate(result.getString("cancelledDate")));
                break;   
                
            case "workStartDate":
                inquiry.setWorkStartDate(result.getString("workStartDate") == null ? null : DateFormatter.convertToSystemDate(result.getString("workStartDate")));
                break;
            case "workEndDate":
                inquiry.setWorkEndDate(result.getString("workEndDate") == null ? null : DateFormatter.convertToSystemDate(result.getString("workEndDate")));
                break;
            case "helpFinderUserId":
                long userId = result.getLong("helpFinderUserId");
                BasicUser user = null;
                if(fetchFullUserDetails)
                    user = getUser(userId);
                else {
                    user = new HelpFinderUser();
                    user.setUserId(userId);
                }
                
                inquiry.setHelpFinderUser(user);
                break;
            case "workerUserId":
                long workerUserId = result.getLong("workerUserId");
                BasicUser workerUser = null;
                if(fetchFullUserDetails)
                    workerUser = getUser(workerUserId);
                else {
                    workerUser = new WorkerUser();
                    workerUser.setUserId(workerUserId);
                }
                
                inquiry.setWorkerUser(workerUser);
                break;
            case "hiredDate":
                inquiry.setHiredDate(result.getString("hiredDate") == null ? null : DateFormatter.convertToSystemDate(result.getString("hiredDate")));
                break;
            case "isHired":
                inquiry.setIsHired(result.getInt("isHired") == 1 ? true : false);
                break;
            case "workDescription":
                inquiry.setWorkDescription(result.getString("workDescription") );
                break;
            case "distanceFoundAwayfrom":
                inquiry.setDistanceFoundAwayfrom(result.getDouble("distanceFoundAwayfrom") );
                break;
            case "insertedOn" :
                inquiry.setInsertedOn(result.getString("insertedOn") == null ? null : DateFormatter.convertToSystemDate(result.getString("insertedOn")));
                break;
            case "updatedOn":
                inquiry.setUpdatedOn(result.getString("updatedOn") == null ? null : DateFormatter.convertToSystemDate(result.getString("updatedOn")));
                break;
            default: break;
            
                   
            }
            
        }
        return inquiry;
    }
    
    @Override
    /***
     * Gets the work inquiry for the from helpuser to  another worker,
     */    
    public List<WorkInquiry> getWorkInquirySentToWorkerId(long fromHelpUserId, long toWorkUserId, boolean fetchFullUserDetails )
            throws RepositoryCreationException {
        
        try
        {
            
            return (List<WorkInquiry>)databaseRepository.executeSelectQuery(selectInquirySentFromUserToWorker, (statement)->{
                try {
                       //set the parameters in the sql query
                      statement.setLong(1, toWorkUserId);
                      statement.setLong(2, fromHelpUserId);  
                    }
                    catch(SQLException ex)
                    {
                        throw new RepositoryCreationException("Error getting inquiry "+ ex.getMessage(), SQLiteUserRepository.class);
                    }    
                },
                (result) ->{
                             
                      List<WorkInquiry> inquiries  = new ArrayList<WorkInquiry>();
                       try {
                           //only interested in the first record
                           while(result != null && result.next()) {                            
                                inquiries.add(createWorkInquiryFromResultSet(result, fetchFullUserDetails));
                                                                            
                           }
                       } catch (SQLException | ParseException ex) {
                              throw new RepositoryCreationException("Error fetching result set  "+ ex.getMessage(), SQLiteUserRepository.class);
                       }
                       return inquiries;
                });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting work inquiry "+ ex.getMessage(), SQLiteUserRepository.class);           
        }
        
    }
    
    
    
    /***
     * Gets the work inquiry received by a user
     * fetchFullUserDetails when set true will return all the user details of worker and the helpfinder user
     */
    public List<WorkInquiry> getWorkInquiryReceivedByUser(long workUserId, boolean fetchFullUserDetails )
            throws RepositoryCreationException {
        
        try
        {
            
            return (List<WorkInquiry>)databaseRepository.executeSelectQuery(selectInquiryReceivedByUser, (statement)->{
                try {
                       //set the parameters in the sql query
                        statement.setLong(1, workUserId);                                
                    }
                    catch(SQLException ex)
                    {
                        throw new RepositoryCreationException("Error getting user "+ ex.getMessage(), SQLiteUserRepository.class);
                    }    
                },
                (result) ->{
                             
                      List<WorkInquiry> inquiries  = new ArrayList<WorkInquiry>();
                       try {
                           
                           //fetch the records and add in inquiries                            
                           while(result != null && result.next()) {                            
                                inquiries.add(createWorkInquiryFromResultSet(result, fetchFullUserDetails));
                                                                            
                           }
                       } catch (SQLException | ParseException ex) {
                              throw new RepositoryCreationException("Error fetching result set  "+ ex.getMessage(), SQLiteUserRepository.class);
                       }
                       return inquiries;
                });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting work inquiry "+ ex.getMessage(), SQLiteUserRepository.class);           
        }
        
    }
    
    /***
     * * gets the total work inquires sent by a user to another work user
     * @param helpFinderUserId
     * @param WorkInquiryId
     * @return int the total count
     */
    public int getTotalInquires(long helpFinderUserId, long workUserId) throws RepositoryCreationException{
        
        try
        {
            
            return (int)databaseRepository.executeSelectQuery(selectTotalInquiries, (statement)->{
                try {
                       //set the parameters in the sql query
                      statement.setLong(1, helpFinderUserId);
                      statement.setLong(2, workUserId);  
                    }
                    catch(SQLException ex)
                    {
                        throw new RepositoryCreationException("Error getting the count "+ ex.getMessage(), SQLiteUserRepository.class);
                    }    
                },
                (result) ->{ 
                     
                    try {
                           //only interested in the first record
                           if(result != null && result.next())                             
                                return result.getInt("totalCount");                                                                                                    
                           
                       } catch (SQLException ex) {
                              throw new RepositoryCreationException("Error fetching result set  "+ ex.getMessage(), SQLiteUserRepository.class);
                       }
                       return 0;
                });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting count"+ ex.getMessage(), SQLiteUserRepository.class);           
        }
        
    }

}


   