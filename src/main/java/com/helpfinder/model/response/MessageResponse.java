/***
 * This class is used for jwt user authentication 
 * 
 * reference taken from https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
 */

package com.helpfinder.model.response;

public class MessageResponse {
  private String message;

  public MessageResponse(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
