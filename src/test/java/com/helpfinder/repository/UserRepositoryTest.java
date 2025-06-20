/*
 
 
  Test class to verify UserRepository
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;

@TestInstance(Lifecycle.PER_CLASS)
//Test class to verify UserRepository
public class UserRepositoryTest {
    
    @Mock
    DatabaseRepository databaseRepo;
    @Mock
    CoreWorkerSkillRepository workerSkillRepository;
    @Mock
    OpenstreetMapLocationRepository locationRepo;
    
    SQLiteUserRepository<BasicUser> userRepo;
    
    HashMap<Integer, WorkerSkill> skills = new HashMap<Integer, WorkerSkill>();
    
    @BeforeEach
    public void setup()
    {
        this.skills.put(1, new WorkerSkill(1, "HandyMan"));
        this.skills.put(2, new WorkerSkill(2, "Painter"));
        this.skills.put(3,new WorkerSkill(3, "Cooking"));
        this.skills.put(4,new WorkerSkill(4, "Housecleaning"));
        this.skills.put(5,new WorkerSkill(5, "Maintenance"));
        this.skills.put(6,new WorkerSkill(6, "Plumber"));
        this.skills.put(7,new WorkerSkill(7, "Electrician"));
        this.skills.put(8,new WorkerSkill(8, "Carpenter"));
        this.skills.put(9,new WorkerSkill(9, "SnowRemoval"));
        this.skills.put(10,new WorkerSkill(10, "Demolition"));
        this.skills.put(11,new WorkerSkill(11, "DebrisRemoval"));
        this.skills.put(12,new WorkerSkill(12, "UnloadingAndLoading"));
        this.skills.put(13,new WorkerSkill(13, "ForkliftOperation"));
        this.skills.put(14,new WorkerSkill(14, "HandToolsPowerTools"));
        this.skills.put(15,new WorkerSkill(15, "FarmAndFieldWork"));
        this.skills.put(16,new WorkerSkill(16, "Catering"));
        this.skills.put(17,new WorkerSkill(17, "GrassCutting"));
        this.skills.put(18,new WorkerSkill(18, "Construction"));
        this.skills.put(19,new WorkerSkill(19, "SecurityGuard"));
        this.skills.put(20,new WorkerSkill(20, "Chauffeur"));
        this.skills.put(21,new WorkerSkill(21, "Butler"));
        this.skills.put(22,new WorkerSkill(22, "Janitor"));
        this.skills.put(23,new WorkerSkill(23, "AthleticTrainer"));
           
           // create all mocks required in sqlliteuserrepository
        locationRepo = new OpenstreetMapLocationRepository();
        databaseRepo = Mockito.mock(SqlliteRepository.class) ;
        workerSkillRepository = Mockito.mock(CoreWorkerSkillRepository.class);
        //when(workerSkillRepository.getAllSkillsets()).thenReturn((List<WorkerSkill>)skills.values().iterator());
        // create instance of userRepo using mock dependency injection
        userRepo = new  SQLiteUserRepository<BasicUser>(locationRepo, workerSkillRepository, databaseRepo);
        try {
            when(databaseRepo.getConnection()).thenReturn(Mockito.mock(Connection.class));
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
  
    @Test
    public void test_Create_new_WorkerUser_should_have_userid()
    {   
        WorkerUser workerUser = new WorkerUser();
        ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
        workerUserSkills.add(new WorkerSkill(1, "HandyMan"));
        workerUser.setUserInformation("1600 Pennsylvania Avenue NW, Washington, DC 20500", "Peter", "h", 
                      "peterh@test.com", EUserType.ROLE_WORKER_USER,
                workerUserSkills);    
       try {
               //mock the behavior of executeInsertQuery in databaseRepo , the id return will be 568
            when(databaseRepo.executeInsertQuery(any(), any(), any())).thenReturn(568);
            //mock the behavior of exeuteSelectQuery to not return a user
            when(databaseRepo.executeSelectQuery(any(), any(), any())).thenReturn(null);
             BasicUser user = userRepo.createUser(workerUser);
             assertEquals(user.getUserId(), 568);
                 
          } catch (Exception e) {                     
                 e.printStackTrace();
          }     
    
    }

    @Test
    public void test_Create_new_HelpFinderUser_should_have_userid()
    {   
           HelpFinderUser helpFinderUser = new HelpFinderUser();
        ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
        workerUserSkills.add(new WorkerSkill(1, "HandyMan"));
        helpFinderUser.setUserInformation("1600 Pennsylvania Avenue NW, Washington, DC 20500", "Peter", "h", 
                      "peterh@test.com", EUserType.ROLE_HELPFINDER_USER);
       
        
       try {
               //mock the behavior of executeInsertQuery in databaseRepo , the id return will be 655
            when(databaseRepo.executeInsertQuery(any(), any(), any())).thenReturn(655);
            //mock the behavior of exeuteSelectQuery to not return a user
            when(databaseRepo.executeSelectQuery(any(), any(), any())).thenReturn(null);
                     BasicUser user = userRepo.createUser(helpFinderUser);
                     assertEquals(user.getUserId(), 655);
                 
          } catch (Exception e) {                     
                 e.printStackTrace();
          }        
        
    }
    

    @Test
    public void test_Create_new_HelpFinderUser_thows_UserExistException_userExist() {   
        HelpFinderUser helpFinderUser = new HelpFinderUser();
        ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
        workerUserSkills.add(new WorkerSkill(1, "HandyMan"));
        helpFinderUser.setUserInformation("1600 Pennsylvania Avenue NW, Washington, DC 20500", "Peter", "h", 
                      "peterh@test.com", EUserType.ROLE_HELPFINDER_USER);       
       
        boolean caughtUserExistException = false;
        try {
               //mock the behavior of executeInsertQuery in databaseRepo , the id return will be 655
            when(databaseRepo.executeInsertQuery(anyString(),  any(), any())).thenReturn(655);
            //mock the behavior of exeuteSelectQuery return a user which should initiate an exception
            when(databaseRepo.executeSelectQuery(any(), any(), any())).thenReturn(helpFinderUser);
                 userRepo.createUser(helpFinderUser);
                 
              }
            catch(UserExistException w)
            {
               caughtUserExistException = true;
            }
            catch (Exception e) {                     
                     e.printStackTrace();
            }        
            assertTrue(caughtUserExistException);
      }
    
    

}
