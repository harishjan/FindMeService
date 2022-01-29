/*
 * BU Term project for cs622
 
  Test class to verify UserRepository
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;

//Test class to verify UserRepository
public class UserRepositoryTest {
    
    GoogleLocationRepository locationRepo;
    DatabaseRepository databaseRepo;
    DataSource dataSource;
    SQLiteUserRepository<BasicUser> userRepo;
    CoreWorkerSkillRepository workerSkillRepository;
    @Before
    public void setup()
    {
        locationRepo = new GoogleLocationRepository();
        
        databaseRepo = new SqlliteRepository(dataSource);
        workerSkillRepository = new CoreWorkerSkillRepository(databaseRepo);
        userRepo = new SQLiteUserRepository<BasicUser>(locationRepo, workerSkillRepository, databaseRepo);
        
    }
    
    @Test
    public void test_Create_new_WorkerUser_should_have_latLong_userid()
    {   
        BasicUser workerUser = new WorkerUser();
        ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
        workerUserSkills.add(workerSkillRepository.getAllSkillsets().get(0));
        ((WorkerUser) workerUser).setUserInformation("test workeruser address", "Peter", "h", 
        		"peterh@test.com", EUserType.ROLE_WORKER_USER,
                workerUserSkills);
        workerUser = userRepo.createUser(workerUser);
        assertEquals( workerUser.getLatLong()[0], 1.1);
        assertEquals( workerUser.getLatLong()[1], 2.2);
        assertNotNull( ((BasicUser) workerUser).getUserId());        
        
    }

    @Test
    public void test_Create_new_HelpFinderUser_should_have_latLong_userid()
    {   
        //test creating new helpfinder user 
    	BasicUser helpFinderUser = new BasicUser();
        helpFinderUser.setUserInformation("test helpfinderuser address", "David", "walt", 
        		"waltDavid@test.com", EUserType.ROLE_HELPFINDER_USER);
        helpFinderUser = userRepo.createUser(helpFinderUser);
        assertEquals( helpFinderUser.getLatLong()[0], 1.1);
        assertEquals( helpFinderUser.getLatLong()[1], 2.2);
        assertNotNull( ((BasicUser) helpFinderUser).getUserId());        
        
    }
    

    

}
