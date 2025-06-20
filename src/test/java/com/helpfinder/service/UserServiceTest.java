/*
 
 
  verify all user service functionalities
 * @author  Harish Janardhanan * 
 * @since   23-jan-2022
 */
package com.helpfinder.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.data.repository.core.RepositoryCreationException;

import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.AdminUser;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.ModeratorUser;
import com.helpfinder.model.UserPermissions;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.repository.CoreWorkerSkillRepository;
import com.helpfinder.repository.DatabaseRepository;
import com.helpfinder.repository.OpenstreetMapLocationRepository;
import com.helpfinder.repository.SQLiteUserRepository;
import com.helpfinder.repository.SqlliteRepository;

@TestInstance(Lifecycle.PER_CLASS)
//test user service related functionalities
public class UserServiceTest {
    
    OpenstreetMapLocationRepository locationRepo;
    @Mock
    DatabaseRepository databaseRepo;
    @Mock
    SQLiteUserRepository<BasicUser> userRepo;
    CoreWorkerSkillRepository workerSkillRepository;
    DataSource dataSource;
    UserService<BasicUser> userService; 
    WorkerUser workerUser;
    HelpFinderUser helpFinderUser;
    AdminUser adminUser;
    ModeratorUser modUser;
    private String selectWorkerSkill = "select WorkerSkillid, workerSkillName from WorkerSkill;";
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
        
        locationRepo = new OpenstreetMapLocationRepository();
        databaseRepo = Mockito.mock(SqlliteRepository.class);
        try {
            when(databaseRepo.executeSelectQuery(eq(selectWorkerSkill), any(), any())).thenReturn(this.skills);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }           
        workerSkillRepository = new CoreWorkerSkillRepository(databaseRepo);
        userRepo = (SQLiteUserRepository<BasicUser>)Mockito.mock(SQLiteUserRepository.class);
        userService = new UserService<BasicUser>(userRepo, locationRepo, null, null);
        
        //test creating new helpfinder user who already is registered
        helpFinderUser = new HelpFinderUser();
           helpFinderUser.setUserInformation("test helpfinderuser address", "John", 
                        "m", "johnm@test.com", EUserType.ROLE_HELPFINDER_USER);
           
           //test creating new worker user who already is registered
           workerUser = new WorkerUser();
           helpFinderUser.setUserInformation("test helpfinderuser address", "John", 
                        "m", "johnm@test.com", EUserType.ROLE_WORKER_USER);
           
           
           //test creating new admin user who already is registered
           adminUser = new AdminUser();
           helpFinderUser.setUserInformation("test helpfinderuser address", "John", 
                        "m", "johnm@test.com", EUserType.ROLE_ADMIN);
           
           modUser = new ModeratorUser();
           helpFinderUser.setUserInformation("test helpfinderuser address", "John", 
                        "m", "johnm@test.com", EUserType.ROLE_MODERATOR);
        
        
    }

    
    /***
     * This test check if the method to create user throws UserExistException if user already exist
     */
    @Test
    public void test_Create_new_user_which_alreadyexist_throws_UserExistException()
    {   
           try {
                     when(userRepo.createUser(any(HelpFinderUser.class))).thenThrow(new UserExistException("UserExist"));
              } catch (RepositoryCreationException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              } catch (UserExistException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
              }
       
        boolean userExistExceptionThrown = false;
        try    {
               
            userService.createUser(helpFinderUser);
        }
        catch(UserExistException ex) {
            userExistExceptionThrown = true;
        }
        assertTrue(userExistExceptionThrown);
        
    }
    
    
    
    /***
     * This test check if the user returned by getbyemail has the correct permission based on usertype
     */
    @Test
    public void test_getUserByEamil_checkif_genericType_returnsthe_CorrectPermission_based_onUsertype()
    {  
             
           //Test checking permissions for helpfinder user
           System.out.println("+++++++++++++++++Test running for generics+++++++++++++++++++++++");
           System.out.println("UserService returning a helpfinder user");
              when(userRepo.findByUsername(anyString())).thenReturn(helpFinderUser);              
              BasicUser user = userService.findByUsername("johnm@test.com");
              //check correct permissions are there
              assertTrue(user.getPermissions().contains(UserPermissions.SEARCH_FOR_WORKERS));
              System.out.println("SEARCH_FOR_WORKERS permission found for Helpfinder user");
              //these permissions should not be there
              assertFalse(user.getPermissions().contains(UserPermissions.ALLOWED_TO_BE_HIRE));
              assertFalse(user.getPermissions().contains(UserPermissions.ADD_ADMIN_USER));
              assertFalse(user.getPermissions().contains(UserPermissions.ARCH_SITE_FEEDBACK));
              assertFalse(user.getPermissions().contains(UserPermissions.DELETE_USER));
              assertFalse(user.getPermissions().contains(UserPermissions.REVIEW_SITE_FEEDBACK));
              System.out.println("Remaining permission not found for helpfinder user");
              System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
              
              System.out.println("UserService returning a worker user");
           //Test checking permissions for Worker user
              when(userRepo.findByUsername(anyString())).thenReturn(workerUser);              
              user = userService.findByUsername("johnm@test.com");
              
              //check correct permissions are there
              assertTrue(user.getPermissions().contains(UserPermissions.ALLOWED_TO_BE_HIRE));
              System.out.println("ALLOWED_TO_BE_HIRE permission found for worker user");
              //these permissions should not be there
              assertFalse(user.getPermissions().contains(UserPermissions.SEARCH_FOR_WORKERS));              
              assertFalse(user.getPermissions().contains(UserPermissions.ADD_ADMIN_USER));
              assertFalse(user.getPermissions().contains(UserPermissions.ARCH_SITE_FEEDBACK));
              assertFalse(user.getPermissions().contains(UserPermissions.DELETE_USER));
              assertFalse(user.getPermissions().contains(UserPermissions.REVIEW_SITE_FEEDBACK));
              System.out.println("Remaining permission not found for workder user");
              
              System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
              System.out.println("UserService returning a admin user");
              //Test checking permissions for Admin user
              when(userRepo.findByUsername(anyString())).thenReturn(adminUser);              
              user = userService.findByUsername("johnm@test.com");
              
              //check correct permissions are there              
              assertTrue(user.getPermissions().contains(UserPermissions.SEARCH_FOR_WORKERS));              
              assertTrue(user.getPermissions().contains(UserPermissions.ADD_ADMIN_USER));
              assertTrue(user.getPermissions().contains(UserPermissions.ARCH_SITE_FEEDBACK));
              assertTrue(user.getPermissions().contains(UserPermissions.DELETE_USER));
              assertTrue(user.getPermissions().contains(UserPermissions.REVIEW_SITE_FEEDBACK));
              System.out.println("SEARCH_FOR_WORKERS, ADD_ADMIN_USER, ARCH_SITE_FEEDBACK, DELETE_USER, REVIEW_SITE_FEEDBACK permission found for admin user");
              //these permissions should not be there              
              assertFalse(user.getPermissions().contains(UserPermissions.ALLOWED_TO_BE_HIRE));       
              System.out.println("Remaining permission not found for admin user");
              
              System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
              System.out.println("UserService returning a moderator user");
              //Test checking permissions for moderator user
              when(userRepo.findByUsername(anyString())).thenReturn(modUser);              
              user = userService.findByUsername("johnm@test.com");
              
              
              //check correct permissions are there
              assertTrue(user.getPermissions().contains(UserPermissions.ARCH_SITE_FEEDBACK));              
              assertTrue(user.getPermissions().contains(UserPermissions.REVIEW_SITE_FEEDBACK));
              assertTrue(user.getPermissions().contains(UserPermissions.SEARCH_FOR_WORKERS));
              
              System.out.println("ARCH_SITE_FEEDBACK, REVIEW_SITE_FEEDBACK, SEARCH_FOR_WORKERS, permission found for moderator user");
              //these permissions should not be there
              assertFalse(user.getPermissions().contains(UserPermissions.ALLOWED_TO_BE_HIRE));                            
              assertFalse(user.getPermissions().contains(UserPermissions.ADD_ADMIN_USER));              
              assertFalse(user.getPermissions().contains(UserPermissions.DELETE_USER));              
              System.out.println("Remaining permission not found for moderator user");
              System.out.println("+++++++++++++++++++End of generics tests+++++++++++++++++++++++++");
              
    }

}
