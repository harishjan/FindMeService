/*
 * BU Term project for cs622
 
  This class is used to store the information of a process  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

//This is the interface which defines a user
public interface User {	
	//getter methods of all properties that a user should have
	String getAddress();
	Double[] getLatLong();
	String getFirstName();
	String getLastName();
	String getEmailAddress();
	EUserEnum getUserType();
	// method to set all the properties
	void setUserInformation(String address, String firstName, String lastname, String emailaddress);

}
