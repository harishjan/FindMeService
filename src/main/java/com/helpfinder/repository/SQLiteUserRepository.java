/*
 * BU Term project for cs622
 
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
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.directory.InvalidAttributesException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.RepositoryCreationException;
import org.springframework.stereotype.Component;

import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.AdminUser;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.ModeratorUser;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;

@Component
// implementation is still not completed
public class SQLiteUserRepository<T extends BasicUser> implements UserRepository<T> {
    //date formatters
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSS");
    private static SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSS");    
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
                     statement.setString(14, dateFormatter.format(Instant.ofEpochMilli((new Date()).getTime())
                               .atZone(ZoneId.systemDefault())
                               .toLocalDateTime()));
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
                             user.setCreatedOn(simpleFormatter.parse(result.getString("insertedOn")));
                             break;
                      case "updatedOn":
                             user.setUpdatedOn(result.getString("updatedOn") == null ? null : simpleFormatter.parse(result.getString("updatedOn")));
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
    public List<WorkInquiry> getUserWorkInquiriesSent(long userId) {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * gets the list of inquiries received by  user
     * @param userId the id of the user
     * @return List<WorkInquiry> List of Work inquiries
     */
    @Override
    public List<WorkInquiry> getUserWorkInquiriesReceived(long userId) {
        //hard coded values for now
        List<WorkInquiry> workInquiries = new ArrayList<>();
        //filter        
        return workInquiries;        
    }

    @Override
    public WorkInquiry addInquiry(WorkInquiry inquiry) {
        // TODO Auto-generated method stub
        return null;
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
    
    
    /**
     * gets all the work inquiries made by the user which are committed by this worker user
     * @param userId the id of the user
     * @return List<WorkInquiry> list of work inquiry which has a committed status as true
     */
    public List<WorkInquiry> getWorkInquiryCommited(Long userId)    {
        
        //Hard coded value, implementation pending
        List<WorkInquiry> workInquiries = getUserWorkInquiriesReceived(userId);
        return workInquiries;
    }
    
    
    /**
     * gets the list of inquiries where user is hired
     * @param userId the id of the user
     * @return List<WorkInquiry> List of Work inquiries
     */
    public List<WorkInquiry> getWorkInquiriesHired(Long userId)    {    
        //hard coded values for now
        List<WorkInquiry> workInquiries = getUserWorkInquiriesReceived(userId)    ;
        return workInquiries;            
        
    }


}
