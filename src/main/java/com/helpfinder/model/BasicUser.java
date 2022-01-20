/*
 * BU Term project for cs622
 
 This abstract class implements user interface which is the base class for any other user types in this system
  This class is used used to define common functionalities in all user types  
 * @author  Harish Janardhanan * 
 * @since   16-Jan-2022
 */
package com.helpfinder.model;


public abstract class BasicUser implements User {
	
	// Stores the userId 
	public long userId;
	// Stores the address of the user
	public String address;
	//stores the latitude and longitude derived from the address
	public Double[] latLong = new Double[2];
	//first name of the user
	public String firstName;
	//last name of the user
	public String lastName;
	// email address of the user
	public String emailAddress;
	// stores information if this is just a normal user using the web site
	// default this set to true
	public EUserEnum userType = EUserEnum.ROLE_HELPFINDER_USER;

	/**
	 * constructor
	 * @param userId user id 
	 * @param address the address of the user 
	 * @param firstName first name of the user
	 * @param lastname last name of the user	
	 * @param emailAddress email address of the user
	 */
	public BasicUser(int userId, String address, String firstName, String lastname, String emailAddress) 
	{
		this.userId = userId;
		this.setUserInformation(address, firstName, lastname, emailAddress);
	}
	
	//empty constructor
	public BasicUser() {} 
	
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
		return latLong;
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
	public EUserEnum getUserType()
	{
		return userType;
	}
	
	/**
	 * This method sets the user information, use this while create a new user
	 * @param userId user id 
	 * @param address the address of the user 
	 * @param firstName first name of the user
	 * @param lastname last name of the user	
	 * @param emailAddress email address of the user
	 */
	@Override
	public void setUserInformation(String address, String firstName, String lastname, String emailAddress)
	{	
		this.address = address;
		this.firstName = firstName;
		this.lastName = lastname;
		this.emailAddress = emailAddress;
	}

}
