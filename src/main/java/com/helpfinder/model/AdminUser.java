package com.helpfinder.model;

import java.util.Arrays;
import java.util.List;

public class AdminUser extends BasicUser {
	
	/***
     * gets the permission a user has based on the role
     * @return Set<UserPermissions> the user permissions
     */
    @Override
    public List<UserPermissions> getPermissions() {
		return Arrays.asList(new UserPermissions[] {    
                UserPermissions.ADD_ADMIN_USER, 
                UserPermissions.ARCH_SITE_FEEDBACK, 
                UserPermissions.DELETE_USER, 
                UserPermissions.REVIEW_SITE_FEEDBACK,
                UserPermissions.SEARCH_FOR_WORKERS
            });
    }

}
