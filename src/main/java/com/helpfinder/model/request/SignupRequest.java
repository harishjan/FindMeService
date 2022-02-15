/***
 * This class is used for jwt user authentication 
 * 
 * reference taken from https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
 */


package com.helpfinder.model.request;

import java.util.List;

import javax.validation.constraints.*;

import com.helpfinder.model.WorkerSkill;

public class SignupRequest {

  @NotBlank
  @Size(max = 50)
  @Email(message = "Email is not valid", regexp = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  @NotBlank
  @Size(min = 6, max = 40)
  private String password;
  
  @NotBlank
  @Size(min = 2, max = 20)
  private String firstName;

  @NotBlank
  @Size(min = 1, max = 20)  
  private String lastName;

  @NotBlank
  @Size(min = 10, max = 100)  
  private String address;
    
  private String userDescription;
  
  private  List<WorkerSkill> skills;

  public void setUserDescription(String userDescription) {
    this.userDescription = userDescription;
  }
  public String getUserDescription() {
    return userDescription;
  }
  
  public List<WorkerSkill> getSkills() {
    return skills;
  }

  public void setSkills(List<WorkerSkill> skills) {
    this.skills = skills;
  }
  

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
}
