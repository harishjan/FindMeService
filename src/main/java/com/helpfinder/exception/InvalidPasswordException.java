
/* 
This class is used to notify a a invalid password 
 * @author  Harish Janardhanan * 
 * @since   07-Feb-2022
 */
//This class is used to notify a invalid password
package com.helpfinder.exception;

public class InvalidPasswordException extends Exception {
       
        public InvalidPasswordException() {}
    /**
     * constructor
     * @param message the message associated with the exception
     */
    public InvalidPasswordException(String message) {
        super(message);
    }

}
