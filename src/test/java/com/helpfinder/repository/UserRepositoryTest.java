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
import java.util.HashSet;
import java.util.Set;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.Test;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.EUserType;
import com.helpfinder.model.User;
import com.helpfinder.model.UserRole;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;

//Test class to verify UserRepository
public class UserRepositoryTest {
    
    GoogleLocationRepository locationRepo;
    DatabaseRepository databaseRepo;
    DataSource dataSource;
    SQLiteUserRepository userRepo;
    CoreWorkerSkillRepository workerSkillRepository;
    @Before
    public void setup()
    {
        locationRepo = new GoogleLocationRepository();
        
        databaseRepo = new SqlliteRepository(dataSource);
        workerSkillRepository = new CoreWorkerSkillRepository(databaseRepo);
        userRepo = new SQLiteUserRepository(locationRepo, workerSkillRepository, databaseRepo);
        
    }
    
    @Test
    public void test_Create_new_WorkerUser_should_have_latLong_userid()
    {    
        //create role
        UserRole userRole = new UserRole();        
        userRole.setUserRole(EUserType.ROLE_WORKER_USER);            
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);
        User workerUser = new WorkerUser();
        ArrayList<WorkerSkill> workerUserSkills = new ArrayList<WorkerSkill>();
        workerUserSkills.add(workerSkillRepository.getAllSkillsets().get(0));
        ((WorkerUser) workerUser).setUserInformation("test workeruser address", "Peter", "h", "peterh@test.com", userRoles,
                workerUserSkills);
        workerUser = userRepo.createUser(workerUser);
        assertEquals( workerUser.getLatLong()[0], 1.1);
        assertEquals( workerUser.getLatLong()[1], 2.2);
        assertNotNull( ((BasicUser) workerUser).getUserId());        
        
    }

    @Test
    public void test_Create_new_HelpFinderUser_should_have_latLong_userid()
    {    //create role
        UserRole userRole = new UserRole();        
        userRole.setUserRole(EUserType.ROLE_HELPFINDER_USER);            
        Set<UserRole> userRoles = new HashSet<>();
        userRoles.add(userRole);
        //test creating new helpfinder user 
        User helpFinderUser = new BasicUser();
        helpFinderUser.setUserInformation("test helpfinderuser address", "David", "walt", "waltDavid@test.com", userRoles);
        helpFinderUser = userRepo.createUser(helpFinderUser);
        assertEquals( helpFinderUser.getLatLong()[0], 1.1);
        assertEquals( helpFinderUser.getLatLong()[1], 2.2);
        assertNotNull( ((BasicUser) helpFinderUser).getUserId());        
        
    }
    

    

}
