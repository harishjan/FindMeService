/*
 * BU Term project for cs622
 
  Concrete class which is the worker user in the system, the method setUserInformation is overloaded here to accept users skills

 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;
import java.util.List;
import java.util.Set;

// the work user implementation from BasicUser
public class WorkerUser extends BasicUser{
    
    //empty constructor
    public WorkerUser()    {        
    }
    
    /**
     * constructor
     * @param userId user id 
     * @param address the address of the user 
     * @param firstName first name of the user
     * @param lastname last name of the user    
     * @param emailAddress email address of the user
     * @param workSkills list of WorkSkill that are specific for this user
     */
    public WorkerUser(long userId, String address, String firstName, String lastname, String emailAddress, Set<UserRole> userRoles, List<WorkerSkill> workSkills)
    {
        super(userId, address, firstName, lastname, emailAddress, userRoles);                
        this.setSkills(workSkills);
    }
    
    
    /**
     * This method is and overloading this method that sets the user information
     * @param userId the id of the user 
     * @param address the address of the user 
     * @param firstName first name of the user
     * @param lastname last name of the user
     * @param workSkills a list of WorkerSkill of the user         
     */
    public void setUserInformation(String address, String firstName, String lastname, String emailAddress, Set<UserRole> userRoles, List<WorkerSkill> workSkills)
    {        
        super.setUserInformation(address, firstName, lastname, emailAddress, userRoles);
        this.setSkills(workSkills);
    }
    

}
