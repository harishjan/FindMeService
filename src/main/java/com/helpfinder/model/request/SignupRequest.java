/***
 * This class is used for jwt user authentication 
 * 
 * reference taken from https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
 */


package com.helpfinder.model.request;

import javax.validation.constraints.*;

public class SignupRequest {

  @NotBlank
  @Size(max = 50)
  @Email
  private String email;

  private String userType;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
  
  @NotBlank
  @Size(min = 10, max = 20)
  private String firstName;

  @NotBlank
  @Size(min = 10, max = 20)  
  private String lastName;

  @NotBlank
  @Size(min = 10, max = 100)  
  private String address;



  public void setAddress(String address) {
    this.address = address;
  }
  public String getAddress() {
    return address;
  }
  
  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }
  
  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUserType() {
    return this.userType;
  }

  public void setUserType(String userType) {
    this.userType = userType;
  }
}
