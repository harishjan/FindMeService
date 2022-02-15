package com.helpfinder.exception;

//this exception is used to notify if an givean address is invalid

public class InvalidAddressException extends Exception {    

        public InvalidAddressException() {}
        
   /**
    * constructor
    * @param message the message associated with the exception
    */
   public InvalidAddressException(String message) {
       super(message);
   }
   
}
