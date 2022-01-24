/*
 * BU Term project for cs622
 
 This class implements user interface which is the base class for any other user types in this system
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

//This class implements user interface which is the base class for any other user types in this system
public class BasicUser implements User {	
	
	// Stores the userId 
	private long userId;
	
	@Size(max = 250)
	// Stores the address of the user
	private String address;	
	
	@Size(max = 20)
	//stores the latitude derived from the address
	private String latitude;
	
	@Size(max = 20)
	//stores the longitude derived from the address
	private String longitude;
		
	@Size(max = 250)
	//first name of the user
	private String firstName;
			
	@Size(max = 250)
	//last name of the user
	private String lastName;
		 
	 @Email
	// email address of the user
	 private String emailAddress;
	 
	// stores the list of user userrole enums	
	 private Set<UserRole> userRoles = new HashSet<>();
	
	//same as email
	@NotBlank
	@Size(max = 20)
	private String userName;
	
	//password set by the user
	@NotBlank
	@Size(max = 120)
	private String password;

	//the property which stores all skills sets of this user
	private List<WorkerSkill> workSkills = new ArrayList<WorkerSkill>();
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
		this.userId = userId;
		this.userName = emailAddress;
		this.setUserInformation(address, firstName, lastname, emailAddress, userRoles);
	}
	
	//empty constructor
	public BasicUser() {} 
	
	
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
	public Double[] getLatLong()
	{
		Double[] latLongValues = new Double[2];		
		if(latitude != null && longitude != null) {
			latLongValues[0] = Double.valueOf(latitude);
			latLongValues[1] = Double.valueOf(longitude);
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
	 *@return EUserEnum gives the type of user
	 * */
	@Override
	public Set<UserRole> getRoles()
	{
		return userRoles;
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
	 */
	@Override
	public void setUserInformation(String address, String firstName, String lastname, String emailAddress, Set<UserRole> userRoles)
	{	
		this.address = address;
		this.firstName = firstName;
		this.lastName = lastname;
		this.emailAddress = emailAddress;
		this.userRoles = userRoles;
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
	 *@param Double[] latitude and longitude of the users address
	 * */
	public void setLatLong(Double[] latLong)
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
	 * set the enum value of the user roles
	 *@param EUserEnum gives the type of user
	 * */
	@Override
	public void setRoles(Set<UserRole> userRoles)
	{
		this.userRoles = userRoles;
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
	
	
	
}
