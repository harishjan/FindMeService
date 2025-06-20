package com.helpfinder.exception;

// this exception is throw if in case the search preference is not able to fetch 
public class InvalidSearchPreferenceException extends Exception{
    

    public InvalidSearchPreferenceException() {}
    
    /**
    * constructor
    * @param message the message associated with the exception
    */
    public InvalidSearchPreferenceException(String message) {
       super(message);
    }

}
