
/***
 * This class is used for jwt user authentication 
 * this class is an implementation of org.springframework.security.core.userdetails
 * reference taken from https://github.com/bezkoder/spring-boot-spring-security-jwt-authentication
 */
package com.helpfinder.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.helpfinder.model.SecureUserDetails;
import com.helpfinder.model.User;
import com.helpfinder.repository.UserRepository;

@Service
public class SecureUserService implements UserDetailsService {
    @Autowired
    final UserRepository userRepo;
    public SecureUserService(UserRepository userRepo)    {
        this.userRepo = userRepo;
    }
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if(user == null)
            throw new UsernameNotFoundException("User Not Found with username: " + username);

        return SecureUserDetails.build(user);
    }

}
