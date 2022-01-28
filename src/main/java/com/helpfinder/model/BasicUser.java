/*
 * BU Term project for cs622
 
 This class implements user interface which is the base class for any other basic user types like worker user and help finder users in this system
  This class is used used to define common functionalities in all user types  
 * @author  Harish Janardhanan * 
 * @since   16-Jan-2022
 */

package com.helpfinder.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

//This help finder user and worker users are belong to this implemenation of the calss
public class BasicUser extends AbstractUser {    
    
    /**
     * constructor
     * @param userId user id 
     * @param address the address of the user 
     * @param firstName first name of the user
     * @param lastname last name of the user    
     * @param emailAddress email address of the user
     * @param userRoles list of roles user has got
     */
    public BasicUser(long userId, String address, String firstName, String lastname, String emailAddress, Set<UserRole> userRoles) 
    {
        super(userId, address, firstName, lastname, emailAddress, userRoles);
    }
    
    //empty constructor
    public BasicUser() {} 
    
    
    
}
