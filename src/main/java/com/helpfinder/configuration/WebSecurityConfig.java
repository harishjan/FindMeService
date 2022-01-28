package com.helpfinder.configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.helpfinder.security.jwt.AuthEntryPointJwt;
import com.helpfinder.security.jwt.AuthTokenFilter;
import com.helpfinder.service.SecureUserService;


// web security related classed required to setup setup pipeline lines where certain resources can be opened up
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
    // securedEnabled = true,
    // jsr250Enabled = true,
    prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
     private static final String[] AUTH_WHITELIST = {
            "/swagger-ui/*", "/swagger-ui.html", "/webjars/**", "/v2/**", "/swagger-resources/**","/auth/**", "/v3/api-docs/**",  "/webjars/swagger-ui/**"
     };
     @Autowired
     private SecureUserService secureUserService;
    
    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;
    
    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }
    
    
     
    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder.userDetailsService(secureUserService).passwordEncoder(passwordEncoder());
    }
      
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
      
    @Override
       public void configure(WebSecurity web) {
        web.ignoring()
        .antMatchers(AUTH_WHITELIST).anyRequest();
       }
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
      //  http.authorizeRequests()
        //        .antMatchers(AUTH_WHITELIST).permitAll();
               // .antMatchers("/**/*").denyAll();
        
        http.cors().and().csrf().disable()
        .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .authorizeRequests().antMatchers(AUTH_WHITELIST).permitAll()
        .antMatchers("/api/test/**").permitAll()
        .anyRequest().authenticated().and()
        .httpBasic();;

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}