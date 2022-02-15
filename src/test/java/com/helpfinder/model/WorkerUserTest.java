/*
 * BU Term project for cs622
 
  Test class to verify WorkerUser service
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import org.junit.jupiter.api.Test;



// example of unit test with junit
public class WorkerUserTest {

    @Test
    public void testcreateUser() {

        // create an object of type WorkerUSer
        WorkerSkill skill = new WorkerSkill(1, "handyman");
        ArrayList<WorkerSkill> workerSkills = new ArrayList<WorkerSkill>();
        workerSkills.add(skill);
        
        // set the user attributes
        WorkerUser user = new WorkerUser((long) 1, "test address", "John", "M", 
                      "test@email.com",EUserType.ROLE_WORKER_USER, workerSkills);

        // assert the values
        assertEquals(user.getAddress(), "test address");
        assertEquals(user.getFirstName(), "John");
        assertEquals(user.getLastName(), "M");
        assertEquals(user.getEmailAddress(), "test@email.com");
        assertEquals(user.getSkills(), workerSkills);
        assertTrue(user.getUserType() == EUserType.ROLE_WORKER_USER);
        
    }

}
