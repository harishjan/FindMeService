package com.helpfinder.model;

import java.util.Arrays;
import java.util.List;

public class HelpFinderUser extends BasicUser {
       
       /***
     * gets the permission a user has based on the role
     * @return Set<UserPermissions> the user permissions
     */
    @Override
    public List<UserPermissions> getPermissions() {
              return  Arrays.asList(new UserPermissions[] {
                UserPermissions.SEARCH_FOR_WORKERS
            });
    }

}

