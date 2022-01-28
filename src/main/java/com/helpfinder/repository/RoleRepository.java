/***
 * This class is used for jwt user authentication 
 * 
 * reference taken from https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
 */
package com.helpfinder.repository;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import com.helpfinder.model.EUserRole;
import com.helpfinder.model.UserPermissions;
import com.helpfinder.model.UserRole;

@Component
public interface RoleRepository {
	//user role mapping to permissions kept private 
    public final static HashMap<EUserRole, List<UserPermissions>> rolePermissionMapping = getRolePermissionMapping();
    
    Optional<UserRole> findByName(EUserRole name);
  
    //This method return the mapping between different user roles and permission associated with the role
    //This project is using this mapping from code, to be moved later to db.
    private static HashMap<EUserRole, List<UserPermissions>> getRolePermissionMapping(){
        HashMap<EUserRole, List<UserPermissions>> rolePermission = new HashMap<>();
        rolePermission.put(EUserRole.ROLE_ADMIN,  Arrays.asList(new UserPermissions[] {    
                                    UserPermissions.ADD_ADMIN_USER, 
                                    UserPermissions.ARCH_SITE_FEEDBACK, 
                                    UserPermissions.DELETE_USER, 
                                    UserPermissions.REVIEW_SITE_FEEDBACK,
                                    UserPermissions.SEARCH_FOR_WORKERS
                                }
                            ));
        rolePermission.put(EUserRole.ROLE_HELPFINDER_USER,  Arrays.asList(new UserPermissions[] {
                                    UserPermissions.SEARCH_FOR_WORKERS
                                }
                            ));
        rolePermission.put(EUserRole.ROLE_WORKER_USER,  Arrays.asList(new UserPermissions[] {
                                    UserPermissions.SEARCH_FOR_WORKERS
                                }
                            ));
        rolePermission.put(EUserRole.ROLE_MODERATOR,  Arrays.asList(new UserPermissions[] {
                                    UserPermissions.ARCH_SITE_FEEDBACK,
                                    UserPermissions.REVIEW_SITE_FEEDBACK,
                                    UserPermissions.SEARCH_FOR_WORKERS
                                }
                            ));
        
        return rolePermission;
    }
}
