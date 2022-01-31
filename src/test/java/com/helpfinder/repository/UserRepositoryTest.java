/*
 * BU Term project for cs622
 
  Test class to verify UserRepository
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import java.util.ArrayList;



import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.helpfinder.exception.UserExistException;
import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;
import org.mockito.junit.MockitoJUnitRunner;


//@RunWith(MockitoJUnitRunner.class)  
@TestInstance(Lifecycle.PER_CLASS)
//Test class to verify UserRepository
public class UserRepositoryTest {
    
	@Mock
    DatabaseRepository databaseRepo;    
    CoreWorkerSkillRepository workerSkillRepository;
    @Mock
    OpenstreetMapLocationRepository locationRepo;
    @Mock
    SQLiteUserRepository<BasicUser> userRepo;
    
    @BeforeEach
    public void setup()
    {
    	
    	// create all mocks required in sqlliteuserrepository
        locationRepo = new OpenstreetMapLocationRepository();
        databaseRepo = Mockito.mock(SqlliteRepository.class) ;
        workerSkillRepository = new CoreWorkerSkillRepository(databaseRepo);        
        // create instance of userRepo using mock dependency injection
        userRepo = new SQLiteUserRepository<BasicUser>(locationRepo, workerSkillRepository, databaseRepo);
    }
  
    @Test
    public void test_Create_new_WorkerUser_should_have_userid()
    {   
    	WorkerUser workerUser = new WorkerUser();
        ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
        workerUserSkills.add(workerSkillRepository.getAllSkillsets().get(0));
        workerUser.setUserInformation("1600 Pennsylvania Avenue NW, Washington, DC 20500", "Peter", "h", 
        		"peterh@test.com", EUserType.ROLE_WORKER_USER,
                workerUserSkills);
       
        
    	try {
    		 //mock the behavior of executeInsertQuery in databaseRepo , the id return will be 568
            when(databaseRepo.executeInsertQuery(any(), any())).thenReturn(568);
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
        workerUserSkills.add(workerSkillRepository.getAllSkillsets().get(0));
        helpFinderUser.setUserInformation("1600 Pennsylvania Avenue NW, Washington, DC 20500", "Peter", "h", 
        		"peterh@test.com", EUserType.ROLE_HELPFINDER_USER);
       
        
    	try {
    		 //mock the behavior of executeInsertQuery in databaseRepo , the id return will be 655
            when(databaseRepo.executeInsertQuery(any(), any())).thenReturn(655);
            //mock the behavior of exeuteSelectQuery to not return a user
            when(databaseRepo.executeSelectQuery(any(), any(), any())).thenReturn(null);
			BasicUser user = userRepo.createUser(helpFinderUser);
			assertEquals(user.getUserId(), 655);
			
		} catch (Exception e) {			
			e.printStackTrace();
		}        
        
    }
    

    @Test
    public void test_Create_new_HelpFinderUser_thows_UserExistException_userExist()
    {   
    	HelpFinderUser helpFinderUser = new HelpFinderUser();
        ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
        workerUserSkills.add(workerSkillRepository.getAllSkillsets().get(0));
        helpFinderUser.setUserInformation("1600 Pennsylvania Avenue NW, Washington, DC 20500", "Peter", "h", 
        		"peterh@test.com", EUserType.ROLE_HELPFINDER_USER);
       
        
        boolean caughtUserExistException = false;
    	try {
    		 //mock the behavior of executeInsertQuery in databaseRepo , the id return will be 655
            when(databaseRepo.executeInsertQuery(anyString(), any())).thenReturn(655);
            //mock the behavior of exeuteSelectQuery return a user which should initiate an exceptoin
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
