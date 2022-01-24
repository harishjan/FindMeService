package com.helpfinder.model;

// userrole to a user is mapped using this class 
public class UserRole {
	//reference id
	 private Long userRoleId;
	 private Long userId;
	 // enum int value
	 private int roleId;
	 

	 public UserRole() {

	 }

	  public UserRole(Long userRoleId, EUserRole roleName, Long userId) {
		this.userRoleId = userRoleId;
	    this.roleId = roleName.ordinal();
	    this.userId = userId;
	  }

	  public Long getUserRoleId() {
	    return userRoleId;
	  }
	  
	  public void setUserRoleId(Long userRoleId) {
		    this.userRoleId = userRoleId;
	  }

	  public void setUserId(Long userId) {
	    this.userId = userId;
	  }
	  
	  public Long getUserId() {
		    return this.userId; 
	  }

	  /***
	   * gets the enum value of the roleid
	   * @return EUserRole the enum value of the role
	   */
	  public EUserRole getUserRoleEnum() {
	    return EUserRole.values()[roleId];
	  }

	  public void setUserRole(EUserRole  userRole) {
	    this.roleId = userRole.ordinal();
	  }
}
