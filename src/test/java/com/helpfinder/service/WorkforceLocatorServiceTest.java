/*
 * BU Term project for cs622
 
  Test class to verify workforcelocator service
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.service;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.helpfinder.model.EUserType;
import com.helpfinder.model.HelpFinderUser;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.model.WorkerUser;
import com.helpfinder.repository.CoreWorkerSkillRepository;
import com.helpfinder.repository.DatabaseRepository;
import com.helpfinder.repository.OpenstreetMapLocationRepository;
import com.helpfinder.repository.SQLiteUserRepository;
import com.helpfinder.repository.SqliteWorkForceLocatorRepository;
import com.helpfinder.repository.SqlliteRepository;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.any;

@TestInstance(Lifecycle.PER_CLASS)
public class WorkforceLocatorServiceTest {
       

    @Mock
    SQLiteUserRepository<WorkerUser> userRepo;
    WorkforceLocatorService<WorkerUser> locatorService;
    @Mock
    DatabaseRepository databaseRepo;    
    InquiryEmailNotificationService notificationService;
    List<WorkerSkill> workerUserSkills;    
    List<WorkerUser> workerUsers;
    WorkerUser workerUser;
    CoreWorkerSkillRepository workerSkillRepository;
    @Mock
    OpenstreetMapLocationRepository locationRepo;
    @Mock
    SqliteWorkForceLocatorRepository<WorkerUser> workforceLocatorRepo;
    @Mock
    HelpFinderUser helpFinderUser;    
    @Mock
    DataSource dataSource;
    UserService<WorkerUser> userService;
    private final ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
    @Mock
    PasswordEncoder encoder;
    
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
        locationRepo = Mockito.mock(OpenstreetMapLocationRepository.class);
        workforceLocatorRepo = (SqliteWorkForceLocatorRepository<WorkerUser>)Mockito.mock(SqliteWorkForceLocatorRepository.class);
        encoder = Mockito.mock(PasswordEncoder.class);
        helpFinderUser = Mockito.mock(HelpFinderUser.class);  
        dataSource = Mockito.mock(DataSource.class);
        // create an object of type WorkerUSer
        WorkerSkill skill = new WorkerSkill(1, "handyman");
        ArrayList<WorkerSkill> workerSkills = new ArrayList<WorkerSkill>();
        workerSkills.add(skill);
        //locationRepo = new GoogleLocationRepository();
        
        databaseRepo = Mockito.mock(SqlliteRepository.class);
        try {
            when(databaseRepo.executeSelectQuery(eq(selectWorkerSkill), any(), any())).thenReturn(this.skills);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }        
        workerSkillRepository = new CoreWorkerSkillRepository(databaseRepo);
        userRepo = (SQLiteUserRepository<WorkerUser>)Mockito.mock(SQLiteUserRepository.class);
        userService = new UserService<WorkerUser>(userRepo, locationRepo, encoder, null);
        //mock this
        //workforceLocatorRepo = new SqliteWorkForceLocatorRepository<HelpFinderUser>(userRepo);
        notificationService = new InquiryEmailNotificationService();
        locatorService = new WorkforceLocatorService<WorkerUser>(workforceLocatorRepo, userService, notificationService);
        //create user and work skills for testing
        helpFinderUser = new HelpFinderUser();
        helpFinderUser.setUserId(1);
        helpFinderUser.setUserInformation("test helpfinderuser address", "David", "walt",
                      "waltDavid@test.com", EUserType.ROLE_HELPFINDER_USER);
        //helpFinderUser = userRepo.createUser(helpFinderUser);
    
        workerUserSkills = new ArrayList<WorkerSkill>();
        workerUserSkills.add(workerSkillRepository.getAllSkillsets().get(0));        
        workerUser = new WorkerUser();
        workerUser.setUserInformation("test worker address", "matt", "m",
                      "mattm@test.com", EUserType.ROLE_HELPFINDER_USER);
        workerUser.setUserId(1);
        workerUsers = new ArrayList<WorkerUser>();
        workerUsers.add(workerUser); 
        System.setOut(new PrintStream(outputStreamCaptor));
        //create mock
        MultiValuedMap<Double, WorkerUser> workForces  = new ArrayListValuedHashMap<>();
        workForces.put(1.1, workerUser);
        //return the mock object
        when(workforceLocatorRepo.findWorkforceForSkills(any(double[].class), anyList(),  any(double.class))).thenReturn(workForces);

    }
    
    @Test
    public void test_findWorder_within_1_mile()
    {    
           
        List<String> skills = workerUserSkills.stream().map(item -> item.getSkillName()).collect(Collectors.toList());
        MultiValuedMap<Double, WorkerUser> users = locatorService.findWorkforce(helpFinderUser.getLatLong(), skills, 1.0);
        //check size and email address returned are correct
        assertEquals(users.size(), 1);        
        assertEquals(((User)users.values().toArray()[0]).getEmailAddress(), "mattm@test.com");
    }
    @Test
    public void test_send_workinquiry_flow_until_hire() throws IOException
    {    
        List<String> skills = workerUserSkills.stream().map(item -> item.getSkillName()).collect(Collectors.toList());
        MultiValuedMap<Double, WorkerUser> users = locatorService.findWorkforce(helpFinderUser.getLatLong(), skills, 1.0);
        assertEquals(users.size(), 1);    
        assertNotNull(users.values());
        assertEquals(((User)users.values().toArray()[0]).getEmailAddress(), "mattm@test.com");
        List<WorkInquiry> workInquiries = new ArrayList<WorkInquiry>();                
        WorkInquiry inquiry1 = null;
        // create work inquiries to send to all users returned by work force locator
        for(User user: users.values()){
            // send a work inquiry to this user.
            // using dummy data
            //create a work inquiry        
             inquiry1 = new WorkInquiry(10, new Date(System.currentTimeMillis()),
                     new Date(new Date(System.currentTimeMillis()).getTime()  +  172800 * 1000), // added 2 days
                        helpFinderUser, user);
            workInquiries.add(inquiry1);
                            
        }
        //send inquiry to all users
        locatorService.sendWorkInquiry(workInquiries);
        
        assertTrue(outputStreamCaptor.toString().trim().startsWith("Email Sent requesting Work from waltDavid@test.com"));
        
        outputStreamCaptor.flush();
        //let each user commit to the work
        for(User user: users.values()){
            // commit the work
            // using dummy data                                
            locatorService.commitWork(inquiry1, true);
            assertTrue( outputStreamCaptor.toString().trim().endsWith("mail sent to waltDavid@test.com informing that user mattm@test.com committed the work"));
                            
        }
        
    
        //let the user hire the one user
        for(User user: users.values()){
            // commit the work
            // using dummy data                                
            locatorService.hireWork(inquiry1, true);
            assertTrue(outputStreamCaptor.toString().trim().endsWith("Email sent to mattm@test.com informing that the waltDavid@test.com has hired the user for the work"));            
            break;
                            
        }
        
    }

    

}
