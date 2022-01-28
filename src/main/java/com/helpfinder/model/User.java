/*
 * BU Term project for cs622
 
  This class is used to store the information of a process  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import java.util.List;
import java.util.Set;

//This is the interface which defines a user
public interface User {    
    //getter/setter methods of all properties that a user should have
    public String getEmailAddress();
    public String getAddress();
    public Double[] getLatLong();
    public String getFirstName();
    public String getLastName();
    public EUserType getUserType();
    public List<WorkInquiry> getInquiryHired();
    public List<WorkInquiry> getWorkInquiriesSent();
    public List<WorkerSkill> getSkills();
    public String getUserName();
    public long getUserId();
    public String getPassword();
    public void setUserInformation(String address, String firstName, String lastname, String emailAddress, EUserType userType);
    public void setPassword(String password);
    public void setEmailAddress(String emailAddress);
    public void setAddress(String address);
    public void setLatLong(Double[] latLong);
    public void setFirstName(String firstName);
    public void setLastName(String lastName);
    public void setUserType(EUserType userType);
    public void setSkills(List<WorkerSkill> workSkils);
    public void setUserId(long userId);
    public void setUserName(String userName);
    public boolean hasPermission(UserPermissions permission);
    public Set<UserPermissions> getPermissions();
    public boolean isAllowedToTakeWork();

}
