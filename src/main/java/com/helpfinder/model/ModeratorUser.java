package com.helpfinder.model;

import java.util.Arrays;
import java.util.List;

public class ModeratorUser extends BasicUser {
	
	/***
     * gets the permission a user has based on the role
     * @return Set<UserPermissions> the user permissions
     */
    @Override
    public List<UserPermissions> getPermissions() {
		return Arrays.asList(new UserPermissions[] {
                UserPermissions.ARCH_SITE_FEEDBACK, 
                UserPermissions.REVIEW_SITE_FEEDBACK,
                UserPermissions.SEARCH_FOR_WORKERS
            });
    }

}
