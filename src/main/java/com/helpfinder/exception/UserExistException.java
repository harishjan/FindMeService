/*
 * BU Term project for cs622
This class is used to notify a user already exist while creating a new user 
 * @author  Harish Janardhanan * 
 * @since   23-Jan-2022
 */
package com.helpfinder.exception;

//this exception is used when a user already exist while creating a new user
public class UserExistException extends Exception {    

	 public UserExistException() {}
	 
    /**
     * constructor
     * @param message the message associated with the exception
     */
    public UserExistException(String message) {
        super(message);
    }
    
}
