
/*
 * BU Term project for cs622
 
  Unit test for HelpFinderUser
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

//not implemented
public class HelpFinderUserTests {

        @Test
        public void testNewInstanceOfBasicUser() {
            //create role
            UserRole userRole = new UserRole();
            userRole.setUserId((long)1);
            userRole.setUserRole(EUserRole.ROLE_HELPFINDER_USER);
            Set<UserRole> userRoles = new HashSet<>();
            userRoles.add(userRole);
            
            //create instance of user
            BasicUser user = new BasicUser((long) 1, "test address", "John", "M", "test@email.com", userRoles);
            // assert the values
            assertEquals(user.getAddress(), "test address");
            assertEquals(user.getFirstName(), "John");
            assertEquals(user.getLastName(), "M");
            assertEquals(user.getEmailAddress(), "test@email.com");
            assertTrue(user.getRoles().contains(userRole));            
        }
        
        

}
