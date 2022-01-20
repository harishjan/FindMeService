package com.helpfinder.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.helpfinder.repository.GoogleLocationRepository;
import com.helpfinder.repository.SQLiteUserRepository;
import com.helpfinder.service.UserService;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration 
//@EnableWebMvc
//@EnableSwagger2
public class Config {
	@Autowired
	public GoogleLocationRepository locationServiceRepo;
	
	 @Bean	   
	 public SQLiteUserRepository userRepo() {
	        return new SQLiteUserRepository(locationServiceRepo);
	 }
	 
	 @Bean	   
	 public UserService userService() {
	        return new UserService(userRepo());
	 }
	/* @Bean
	   public Docket productApi() {
	      return new Docket(DocumentationType.SWAGGER_2).select()
	         .apis(RequestHandlerSelectors.basePackage("com.helpfinder.")).build();
	   }
	*/
}
