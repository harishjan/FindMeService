package com.helpfinder.repository;


import java.util.Optional;
import org.springframework.stereotype.Component;

import com.helpfinder.model.EUserRole;
import com.helpfinder.model.UserRole;

//This class exposes functionality related to User roles
@Component
public class UserRoleRespository implements RoleRepository{

    
    /**
     * gets UserRole instance based on the role name
     * @param name EUserRole name
     */
    @Override
    public Optional<UserRole> findByName(EUserRole name) {
        
        UserRole userRole = new UserRole();
        switch(name) {
            case ROLE_ADMIN:
                userRole.setUserRole(name);
                userRole.setUserRoleId((long)1);
                break;
            case ROLE_MODERATOR:
                userRole.setUserRole(name);;
                userRole.setUserRoleId((long)2);
                break;
            case ROLE_WORKER_USER:
                userRole.setUserRole(name);
                userRole.setUserRoleId((long)3);
                break;
            case ROLE_HELPFINDER_USER:
                userRole.setUserRole(name);
                userRole.setUserRoleId((long)4);
                break;
            default: 
                userRole.setUserRole(EUserRole.ROLE_HELPFINDER_USER);
                userRole.setUserRoleId((long)4);
        }
        return Optional.ofNullable(userRole);
    }

}

