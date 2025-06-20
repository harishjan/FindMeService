/* 
 
 This class implements user interface which is the base class for any other basic user types like worker user and help finder users in this system
  This class is used used to define common functionalities in all user types  
 * @author  Harish Janardhanan * 
 * @since   16-Jan-2022
 */

package com.helpfinder.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;

import com.fasterxml.jackson.annotation.JsonIgnore;

//Other user types are implemented from this class 
public abstract class BasicUser implements User {    
    
    /**
     * constructor
     * @param userId user id 
     * @param address the address of the user 
     * @param firstName first name of the user
     * @param lastname last name of the user    
     * @param emailAddress email address of the user
     * @param EUserType user type
     */
    public BasicUser(long userId, String address, String firstName, String lastname, String emailAddress, EUserType userType) {
              this.userId = userId;
           this.userName = emailAddress;
           this.setUserInformation(address, firstName, lastname, emailAddress, userType);
    }
    //empty constructor
    public BasicUser() {} 
    
    
 // Stores the userId 
    private long userId;
    
    // Stores the address of the user
    private String address;    
    
    //stores the latitude derived from the address
    private String latitude;
    
    //stores the longitude derived from the address
    private String longitude;
        
    //first name of the user
    private String firstName;
            
    //last name of the user
    private String lastName;
         
  
    // email address of the user
     private String emailAddress;
     
    // stores the user type  
     private EUserType userType;
    
    //same as email
    private String userName;
    
    //password set by the user
    @JsonIgnore
    private String password;
    
    //Date when this user was created  
    private Date createdOnDate;
    
    
    //Date when user was last updated
    private Date updatedOnDate;
    
    // users work description
    private String userDescription;
    
    //cosine and sine values for lat and long used for distance calculations
    private Double cosLatRadians;
    private Double sinLatRadians;
    private Double cosLongRadians;
    private Double sinLongRadians;
    

    //the property which stores all skills sets of this user
    private List<WorkerSkill> workSkills = new ArrayList<WorkerSkill>();
    
    
    //getters
    /**
     * Gets the email address of the user
     *@return String email address of the user
     * */
    public String getEmailAddress()
    {
        return emailAddress;
    }
    
    /**
     * Gets the address of the user
     *@return String the address of the user
     * */
    public String getAddress()
    {
        return address;
    }
    
    /**
     * Gets the latitude and longitude of the users address
     *@return Double[] latitude and longitude of the users address
     * */
    public double[] getLatLong()
    {
        double[] latLongValues = new double[2];        
        if(latitude != null && longitude != null) {
            latLongValues[0] = (double)Double.valueOf(latitude);
            latLongValues[1] = (double)Double.valueOf(longitude);
        }
        return  latLongValues;
    }
    
    /**
     * Gets the first name of the user
     *@return String  first name of the user
     * */
    public String getFirstName()
    {
        return firstName;
    }
    
    /**
     * Gets the last name of the user
     *@return String  last name of the user
     * */
    public String getLastName()
    {
        return lastName;
    }
    
    /**
     * get the enum value of the type of user
     *@return EUserType user type
     * */
    @Override
    public EUserType getUserType()
    {
        return userType;
    }
    
    
    
    /**
     * gets the list of inquiries made where the work has been hired
     * 
     * @return List<WorkInquiry> List of Work inquiries where the work has been
     *         hired
     */
    @Override
    public List<WorkInquiry> getInquiryHired() {
        // hard coded values for now
        List<WorkInquiry> workInquiries = getWorkInquiriesSent();
        // filter
        return workInquiries;

    }

    /**
     * gets the list of inquiries sent to user
     * 
     * @return List<WorkInquiry> List of Work inquiries
     */
    @Override
    public List<WorkInquiry> getWorkInquiriesSent() {

        // hard coded values for now
        List<WorkInquiry> workInquiries = new ArrayList<>();
        // if(userId == 1)

        return workInquiries;

    }
    
    /**
     * Gets users work skills 
     *@return List<WorkerSkill> this users WorkSkills
     * */
    @Override
    public List<WorkerSkill> getSkills()
    {
        return workSkills;
    }
    
    @Override
    public String getUserName() {
        return userName;
    }
    
    @Override
    public long getUserId() {
        return userId;
    }
    
    @Override
    public String getPassword() {

        return password;
    }
    
    
    //setters
    
    
    /**
     * This method sets the user information, use this while create a new user
     * @param userId user id 
     * @param address the address of the user 
     * @param firstName first name of the user
     * @param lastname last name of the user    
     * @param emailAddress email address of the user
     * @param EUserType user type
     */
    @Override
    public void setUserInformation(String address, String firstName, String lastname, String emailAddress, EUserType userType)
    {    
        this.address = address;
        this.firstName = firstName;
        this.lastName = lastname;
        this.emailAddress = emailAddress;
        this.userType = userType;
    }
    

    
    @Override
    public void setPassword(String password) {

        this.password = password;
    }
    
        
    /**
     * sets the email address of the user
     *@param String email address of the user
     * */
    public void setEmailAddress(String emailAddress)
    {
        this.emailAddress = emailAddress;
    }
    
    /**
     * sets the address of the user
     *@param String the address of the user
     * */
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    /**
     * sets the latitude and longitude of the users address
     *@param double[] latitude and longitude of the users address
     * */
    public void setLatLong(double[] latLong)
    {
        if(latLong == null || latLong.length != 2)
            throw new IllegalArgumentException("latLong array should be of size 2");

        this.latitude = String.valueOf(latLong[0]);
        this.longitude = String.valueOf(latLong[1]);
    }
    
    /**
     * sets the first name of the user
     *@param String  first name of the user
     * */
    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    
    /**
     * sets the last name of the user
     *@param String  last name of the user
     * */
    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }
    
    /**
     * set the enum value of the user type
     *@param EUserType user type
     * */
    @Override
    public void setUserType(EUserType userType)
    {
        this.userType = userType;
    }
    
    /**
     * set the users work skills 
     *@param List<WorkerSkill> this users WorkSkills
     * */
    @Override
    public void setSkills(List<WorkerSkill> workSkils)
    {
        this.workSkills = workSkils;
    }
    
    @Override
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    /**
     * This will tell if this user is allowed to work
     * A basic user is not allowed to work
     * @return boolean true if allowed to work otherwise false
     */
    public boolean isAllowedToTakeWork() {
           return getPermissions().contains(UserPermissions.ALLOWED_TO_BE_HIRE);
    }
    
    /***
     * checks if user has a certain permissions
     * @param UserPErmission the permission to check for
     * @return boolean true if user has permissions else false
     */
    @Override
    public boolean hasPermission(UserPermissions permission) {       
       return getPermissions().contains(permission);
    }
    
    //set the date created
    public void setCreatedOn(Date date) {
           createdOnDate = date;
    }
    //set the date updated
    public void setUpdatedOn(Date date){
           updatedOnDate = date;
    }
    //get the date created
    public Date getCreatedOn() {
           return createdOnDate;
    }
    //get the date updated
    public Date getUpdatedOn() {
           return updatedOnDate;
    }
    
    /***
     * gets the permission a user has based on the role
     * @return Set<UserPermissions> the user permissions
     */
    @Override
    public List<UserPermissions> getPermissions() {         
           throw new NotImplementedException();
    }
    
    //getter setters for consine and sine values of lat/long
    public Double getCosLatRadians() {
           return this.cosLatRadians;
    }
    
    public Double getSinLatRadians() {
           return this.sinLatRadians;
    }
    public Double getCosLongRadians() {
           return this.cosLongRadians;
    }
    public Double getSinLongRadians() {
           return this.sinLongRadians;
    }
    
    public void setCosLatRadians(Double value) {
           this.cosLatRadians = value;
    }
    public void setSinLatRadians(Double value) {
           this.sinLatRadians = value;
    }
    public void setCosLongRadians(Double value) {
           this.cosLongRadians = value;
    }
    public void setSinLongRadians(Double value) {
           this.sinLongRadians = value;
    }
    
    // get set user work description

        public String getUserDescription()        {               
               return userDescription;
        }
        public void setUserDescription(String desc) {
               this.userDescription = desc;
        }
    
}

    