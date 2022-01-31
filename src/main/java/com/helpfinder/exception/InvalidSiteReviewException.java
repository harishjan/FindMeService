/*
 * BU Term project for cs622
This class is used to notify a site review is invalid 
 * @author  Harish Janardhanan * 
 * @since   23-Jan-2022
 */
//This class is used to notify a site review is invalid
package com.helpfinder.exception;

public class InvalidSiteReviewException extends Exception {
	
	 public InvalidSiteReviewException() {}
    /**
     * constructor
     * @param message the message associated with the exception
     */
    public InvalidSiteReviewException(String message) {
        super(message);
    }

}
