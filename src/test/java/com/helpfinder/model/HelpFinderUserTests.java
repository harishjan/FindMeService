
/*
 * BU Term project for cs622
 
  Unit test for HelpFinderUser
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

//not implemented
public class HelpFinderUserTests {

        @Test
        public void testNewInstanceOfBasicUser() {           
               
            //create instance of user
            BasicUser user = new HelpFinderUser();
            user.setAddress("test address");
            user.setFirstName("John");
            user.setLastName( "M");
            user.setEmailAddress( "test@email.com");
            user.setUserType(EUserType.ROLE_HELPFINDER_USER);            
            // assert the values
            assertEquals(user.getAddress(), "test address");
            assertEquals(user.getFirstName(), "John");
            assertEquals(user.getLastName(), "M");
            assertEquals(user.getEmailAddress(), "test@email.com");
            assertTrue(user.getUserType() == EUserType.ROLE_HELPFINDER_USER);            
        }
        
        

}
