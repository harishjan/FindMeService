/***
 * this class is an implementation of  org.springframework.security.core.userdetails
 * This class is used for jwt user authentication 
 * 
 * reference taken from https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
 */


package com.helpfinder.model;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;

//this class is an implementation of  org.springframework.security.core.userdetails
//This class is used for jwt user authentication [implementation is pending]
public class SecureUserDetails implements UserDetails {
  private static final long serialVersionUID = 1L;

  private Long id;

  private String username;
  private String firstName;
  private String lastName;


  private String email;

  @JsonIgnore
  private String password;

  private Collection<? extends GrantedAuthority> authorities;

  
  public SecureUserDetails(Long id, String username, String email, String password, String firstName, String lastName,
      Collection<? extends GrantedAuthority> authorities) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
  }

  public static SecureUserDetails build(User user) {
         Set<GrantedAuthority> authorities = user.getPermissions().stream().map(p -> new SimpleGrantedAuthority(p.name())).collect(Collectors.toSet());                      
            
    return new SecureUserDetails(
        user.getUserId(), 
        user.getUserName(), 
        user.getEmailAddress(),
        user.getPassword(), 
        user.getFirstName(),
        user.getLastName(),
        authorities);
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }
  

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public Long getId() {
    return id;
  }

  public String getEmail() {
    return email;
  }
  public void setPassword(String password) {
           this.password = password;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    SecureUserDetails user = (SecureUserDetails) o;
    return Objects.equals(id, user.id);
  }
  
  

}
