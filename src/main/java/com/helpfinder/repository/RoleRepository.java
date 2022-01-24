/***
 * This class is used for jwt user authentication 
 * 
 * reference taken from https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
 */
package com.helpfinder.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import com.helpfinder.model.EUserRole;
import com.helpfinder.model.UserRole;

@Component
public interface RoleRepository {
  Optional<UserRole> findByName(EUserRole name);
}
