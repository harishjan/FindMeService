/*
 * BU Term project for cs622
 
  This class is the data access layer to get work force location related functionalities using sqlite as the db
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */

package com.helpfinder.repository;

import java.security.InvalidParameterException;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.naming.directory.InvalidAttributesException;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.core.RepositoryCreationException;
import org.springframework.stereotype.Component;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.WorkInquiry;

@Component
//This class is still not implemented these are dummy data
public class SqliteWorkForceLocatorRepository<T extends BasicUser> implements WorkForceLocatorRepository<T> {

    // dummy data for testing
    static HashMap<Integer, WorkInquiry> dummaryWorkInquiries = new HashMap<Integer, WorkInquiry>();

    // store the user repo instance
    @Autowired
    private UserRepository<T> userRepo;

    @Autowired
    private DatabaseRepository databaseRepository;
    //@Autowired
    public SqliteWorkForceLocatorRepository(UserRepository<T> userRepo, DatabaseRepository databaseRepositor) {
        this.userRepo = userRepo;
        this.databaseRepository = databaseRepositor;

    }

    /***
     * gets top 100 work users who are closes to the given mile radius for the skill set 
     * 
     */
    /***
     * gets top 100 worker users who are closes to the given mile radius for the skill sets that being searched for
     * @param latlong
     * @param skills the list of skill names
     * @param mileRadius the distance in miles
     * @returns multiplevaluemap of distance and the users
     */
    @Override
    public MultiValuedMap<Double, T> findWorkforceForSkills(double[] latLong, List<String> skills, double mileRadius) {
           if(latLong.length <2 || mileRadius <= 0 || skills == null || skills.size() == 0 )
                  throw new InvalidParameterException("Workforce locator input parameter is not valid");
           
           //convert lat long to cosine and sine values and create the query.
           String sqlQuery = getDistanceQuery(mileRadius, WorkForceLocatorRepository.getCosRadians( latLong[0]) , WorkForceLocatorRepository.getSinRadians(latLong[0])
                         , WorkForceLocatorRepository.getCosRadians(latLong[1]), WorkForceLocatorRepository.getSinRadians(latLong[1]), skills);
       
        
        try
        {
            return databaseRepository.executeSelectQuery(sqlQuery, (statement)->{},
                      (result) ->{
                             // map with key as distance and the value as user 
                             MultiValuedMap<Double, T> workForces  = new ArrayListValuedHashMap<Double, T>();
                             T user = null;
                       try {
                             //get all the matching records
                               while(result != null && result.next()) {                                                  
                                      // create the user by type
                                      user = userRepo.createUserByTypeFromResultSet(result, true);
                                      // get the distance calculated by the sql query
                                      Double distanceACos = (double)result.getDouble("distanceACos");
                                      // add the distance which is converted to miles and user in the map
                                            workForces.put(WorkForceLocatorRepository.convertCosToMiles((distanceACos > 1.0) ? 1.0 : distanceACos), user);
                               }
                                           
                       } catch (SQLException | InvalidAttributesException | ParseException ex) {
                              throw new RepositoryCreationException("Error fetching result set  "+ ex.getMessage(), SqliteWorkForceLocatorRepository.class);
                                   }
                       return workForces;
                      });
        }
        catch(SQLException ex)
        {
            throw new RepositoryCreationException("Error getting workforce "+ ex.getMessage(), SqliteWorkForceLocatorRepository.class);           
        }
    }
   
    @Override
    public WorkInquiry getInquiry(int inquiryId) {
        // TODO Auto-generated method stub
        return dummaryWorkInquiries.get(inquiryId);
    }
  
    
    
    /***
     * get the query to select top 100 user who are with in the distance ordered by the distance in descending
     * @param distance
     * @param cosLatRadians
     * @param sinLatRadians
     * @param cosLongRadians
     * @param sinLongRadians
     * @param skills list of string of skill names
     * @return String the sql query
     */
    public String getDistanceQuery(double distance, double cosLatRadians, double sinLatRadians, double cosLongRadians, double sinLongRadians, List<String> skills) {
           
           // create the skills array to string params
           StringBuilder skillsString = new StringBuilder();
           for (String param : skills) 
                  skillsString.append(",").append("'").append(param).append("'");
           
           StringBuilder queryString = new StringBuilder();
           
           // example of the the actual query when it is constructed
        /*"SELECT u.userId, u.address, u.lat, u.long, u.firstName, u.lastName, u.emailAddress, u.userType, u.insertedOn, u.updatedOn , 
                    (0.6504529848669602*"sinLatRadians"-0.7595465189688266*"cosLatRadians"*(-0.9629831009497116*"sinLongRadians"+0.2695617689608033*"cosLongRadians")) 
                    AS "distanceACos" FROM User u INNER JOIN UserWorkSkill w on u.userId = w.userId INNER join WorkerSkill s on w.workerSkillId = s.workerSkillId 
                    WHERE u.userType = "ROLE_WORKER_USER" and (0.6504529848669602 * "sinLatRadians" +0.7595465189688266* "cosLatRadians" * (-0.9629831009497116* "sinLongRadians" 
                    + 0.2695617689608033* "cosLongRadians")) >0.9999999680954567 AND 
                    s.workerskillsearchname in ('handyman' ) ORDER BY "distanceACos" DESC limit 100;;*/
           
           //build using string builder           
           queryString.append("SELECT u.userId, u.address, u.lat, u.long, u.firstName, u.lastName, u.emailAddress, u.userType, u.insertedOn, u.updatedOn , userDescription, (")
           .append(sinLatRadians).append("*\"sinLatRadians\"+").append(cosLatRadians).append("*\"cosLatRadians\"*(")
           .append(sinLongRadians).append("*\"sinLongRadians\"+").append(cosLongRadians).append( "*\"cosLongRadians\")) AS \"distanceACos\" FROM User u ")
           .append("INNER JOIN UserWorkSkill w on u.userId = w.userId ").append("INNER join WorkerSkill s on w.workerSkillId = s.workerSkillId WHERE u.userType = \"ROLE_WORKER_USER\" and (")
           .append(sinLatRadians).append(" * \"sinLatRadians\" +").append(cosLatRadians).append("* \"cosLatRadians\" * (")
           .append(sinLongRadians).append("* \"sinLongRadians\" + ").append(cosLongRadians).append("* \"cosLongRadians\")) >")
           .append(WorkForceLocatorRepository.getDistanceInMilesToCos(distance)).append(" AND s.workerskillsearchname in (").append(skillsString.toString().substring(1)).append(" ) ORDER BY \"distanceACos\" DESC limit 100;");
           
           return queryString.toString();
    }
}
