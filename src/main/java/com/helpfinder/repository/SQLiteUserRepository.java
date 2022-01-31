/*
 * BU Term project for cs622
 
  This class implements the sqlite data access layer for access user related information
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;


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
import java.util.HashMap;
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
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;

@Component
// implementation is still not completed
public class SQLiteUserRepository<T extends BasicUser> implements UserRepository<T> {
    //date formaters
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSS");
    private static SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSSS");
    
    // dummy data for testing
    HashMap<Long, T> dummyUsers = new HashMap<Long, T>();
    // stores the location service repo
    @Autowired
    LocationServiceRepository locationServiceRepo;
    @Autowired
    WorkerSkillRepository workerSkillRepository;
    @Autowired
    DatabaseRepository databaseRepository;
    
    // constructor added to create dummy data
    public SQLiteUserRepository(LocationServiceRepository locationServiceRepo, WorkerSkillRepository workerSkillRepository
            , DatabaseRepository databaseRepository) {
        this.locationServiceRepo = locationServiceRepo;
        this.workerSkillRepository = workerSkillRepository;
        this.databaseRepository = databaseRepository;
        // create some dummy data
        // 1 is a HelpFinderUser
        // 2 and 3 is a WorkerUser
      /*  User user1 = new BasicUser(1, "test address", "John", "m", "johnm@test.com", null);
        user1.setLatLong(new Double[] {1.1,2.2});

        List<WorkerSkill> user2Skills = new ArrayList<WorkerSkill>();
        user2Skills.add(this.workerSkillRepository.getAllSkillsets().get(0));
        user2Skills.add(this.workerSkillRepository.getAllSkillsets().get(1));        
        WorkerUser user2 = new WorkerUser(2, "test2 address", "Matt", "p", "mattm@test.com", null, user2Skills);
        user1.setLatLong(new Double[] {1.2,2.3});

        List<WorkerSkill> user3Skills = new ArrayList<WorkerSkill>();
        user3Skills.add(this.workerSkillRepository.getAllSkillsets().get(0));
        user3Skills.add(this.workerSkillRepository.getAllSkillsets().get(3));
        WorkerUser user3 = new WorkerUser(3, "test3 address", "Micheal", "S", "micheals@test.com", null, user3Skills);
        user1.setLatLong(new Double[] {12.1,16.2});
        dummyUsers.put((long) 1, (T) user1);
        dummyUsers.put((long) 2, (T)user2);
        dummyUsers.put((long) 3,  (T)user3);*/
    }

    @Override
    public T getUser(long userId) {
        // TODO Auto-generated method stub

        return dummyUsers.get((long) userId);
    }
    
    String insertUserQuery = "insert into user (address, lat, long, firstName, lastName, emailAddress, userName, "
            + "password, userType, cosLatRadians, sinLatRadians, cosLongRadians, sinLongRadians, insertedOn) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
    String selectUserQuery = "select userId, address, lat, long, firstName, lastName, emailAddress, userName, "
            + "password, userType, insertedOn, updatedOn from user where emailaddress = ?;";
    /***
     * This method creates a new user and stores it in sqlite
     * @param user is the instance of User object
     * @return user the user instance after setting the latlong and userId
     */

    @Override
    public T createUser(T user) throws RepositoryCreationException, UserExistException {
        
    	if(getByEmail(user.getEmailAddress()) != null)
    		throw new UserExistException("User Already Exist");
        try
        {	
        	
            int userId = databaseRepository.executeInsertQuery(insertUserQuery, (statement)->{
            try {
            
                    statement.setString(1, user.getAddress());                
                    statement.setFloat(2, (float) (user.getLatLong().length < 2 || user.getLatLong()[0] == null ? -1.0 : user.getLatLong()[0].floatValue()));
                    statement.setFloat(3, (float) (user.getLatLong().length < 2  || user.getLatLong()[1] == null ? -1.0 : user.getLatLong()[1].floatValue()));
                    statement.setString(4, user.getFirstName());
                    statement.setString(5, user.getLastName());
                    statement.setString(6, user.getEmailAddress());
                    statement.setString(7, user.getEmailAddress());
                    statement.setString(8, user.getPassword());
                    statement.setString(9, user.getUserType().name());                        
                    statement.setFloat(10, (float) (user.getLatLong().length < 2 || user.getLatLong()[0] == null ? -1.0 :  WorkForceLocatorRepository.getCosRadians(user.getLatLong()[0])));
                    statement.setFloat(11, (float) (user.getLatLong().length < 2 || user.getLatLong()[0] == null ? -1.0 : WorkForceLocatorRepository.getSinRadians(user.getLatLong()[0])));
                    statement.setFloat(12, (float) (user.getLatLong().length < 2  || user.getLatLong()[1] == null ? -1.0 : WorkForceLocatorRepository.getCosRadians(user.getLatLong()[1])));                        
                    statement.setFloat(13, (float) (user.getLatLong().length < 2  || user.getLatLong()[1] == null ? -1.0 : WorkForceLocatorRepository.getSinRadians(user.getLatLong()[1])));
                    statement.setString(14, dateFormatter.format(Instant.ofEpochMilli((new Date()).getTime())
                              .atZone(ZoneId.systemDefault())
                              .toLocalDateTime()));
                }
                catch(SQLException ex)
                {
                    throw new RepositoryCreationException("Error creating user "+ ex.getMessage(), SQLiteUserRepository.class);
                }    
            });
            System.out.println(userId);
            if(userId == -1)
                throw new RepositoryCreationException("Error creating user ", SQLiteUserRepository.class);
            
            user.setUserId(userId);
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error creating user "+ ex.getMessage(), SQLiteUserRepository.class);
        }        
        return user;
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
    public T createUserByTypeFromResultSet(ResultSet result) throws SQLException, InvalidAttributesException, ParseException {
    	
    	//to check if columns exist
    	ResultSetMetaData metadata = result.getMetaData();
    	//get the user by type
    	T user  = createUserByType(EUserType.forName(result.getString("userType")).get(), null);
    	if(user == null)
			throw new InvalidAttributesException("userType column not found");
    	//number of columns
    	int columns = metadata.getColumnCount();
		
		//to store latlong
    	Double[] latLong = new Double[2];
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
		        	latLong[0] = result.getFloat("lat") == -1.0 ? null : (double)result.getFloat("lat");
		        	break;
		        case "long":	        	
		        	latLong[0] = result.getFloat("long") == -1.0 ? null : (double)result.getFloat("long");
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
		        	user.setCosLatRadians(result.getFloat("cosLatRadians") == -1.0 ? null : (double)result.getFloat("cosLatRadians"));
		        	break;
		        case "sinLatRadians":
		        	user.setSinLatRadians(result.getFloat("sinLatRadians") == -1.0 ? null : (double)result.getFloat("sinLatRadians"));
		        	break;
		        case "cosLongRadians":
		        	user.setCosLongRadians(result.getFloat("cosLongRadians") == -1.0 ? null : (double)result.getFloat("cosLongRadians"));
		        	break;
		        case "sinLongRadians":
		        	user.setSinLongRadians(result.getFloat("sinLongRadians") == -1.0 ? null : (double)result.getFloat("sinLongRadians"));
		        	break;
		        case "insertedOn":
		        	user.setCreatedOn(simpleFormatter.parse(result.getString("insertedOn")));
		        	break;
		        case "updatedOn":
		        	user.setUpdatedOn(result.getString("updatedOn") == null ? null : simpleFormatter.parse(result.getString("updatedOn")));
		        	break;
		         default:
		        	 break;
	        }
	        
	    }
	    //store latlong
	    user.setLatLong(latLong);
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
		        				user = createUserByTypeFromResultSet(result);
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
    public List<WorkerSkill> getUserSkills(long userId) {
        // TODO Auto-generated method stub
        User user = dummyUsers.get((long) userId);
        return user.getSkills();
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
