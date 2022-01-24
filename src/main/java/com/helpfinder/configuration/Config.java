package com.helpfinder.configuration;

import javax.sql.DataSource;

import org.hibernate.cfg.Environment;
import org.hibernate.validator.internal.util.stereotypes.Lazy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import com.helpfinder.repository.CoreSiteReviewsRepository;
import com.helpfinder.repository.CoreWorkerSkillRepository;
import com.helpfinder.repository.DatabaseRepository;
import com.helpfinder.repository.GoogleLocationRepository;
import com.helpfinder.repository.LocationServiceRepository;
import com.helpfinder.repository.SQLiteUserRepository;
import com.helpfinder.repository.SiteReviewsRepository;
import com.helpfinder.repository.SqliteWorkForceLocatorRepository;
import com.helpfinder.repository.SqlliteRepository;
import com.helpfinder.repository.UserRepository;
import com.helpfinder.repository.WorkForceLocatorRepository;
import com.helpfinder.repository.WorkerSkillRepository;
import com.helpfinder.service.SecureUserService;
import com.helpfinder.service.UserService;

@Configuration 
//@EnableWebMvc
//@EnableSwagger2
public class Config {
	
/*	
	 
	// create all autowired instances
	@Autowired
	public LocationServiceRepository locationServiceRepo;
	@Autowired
	DataSource	dataSource;	
	@Autowired
	public DatabaseRepository databaseRepo;
	@Autowired
	public WorkerSkillRepository workerSkillRepository;

	@Autowired
	public UserService userService;
	
	@Autowired
	UserRepository userRepo;
	
	
	
	@Autowired
	WorkForceLocatorRepository workForceLocatorRepository;
	@Autowired
	SiteReviewsRepository siteReviewsRepository;
	
	
	@Autowired
	UserRepository userRepository;
	 
	@Autowired
	SecureUserService secureUserService;	
	
	
	
	@Bean   
	 public SwaggerConfig waggerConfig() {
	        return new SwaggerConfig();
	 }
	
	 @Bean   
	 public WebSecurityConfig webSecurityConfig() {
	        return new WebSecurityConfig();
	 }
	 
	 @Bean   
	 public SecureUserService secureUserService() {
	        return new SecureUserService(userRepo);
	 }
	
	
	 @Bean   
	 public LocationServiceRepository locationServiceRepo() {
	        return new GoogleLocationRepository();
	 }
	
	 @Bean	   
	 public UserRepository userRepo() {
	        return new SQLiteUserRepository(locationServiceRepo, workerSkillRepository);
	 }
	 
	 @Bean	   
	 public WorkForceLocatorRepository workForceLocatorRepository() {
	        return new SqliteWorkForceLocatorRepository(userRepo);
	 }
	 
	 @Bean	   
	 public UserService userService() {
	        return new UserService(userRepo);
	 }
	 
	 
	
	 
	 @Bean
	 public DatabaseRepository databaseRepo() {
		 return new SqlliteRepository();
	 }
	 
//	
	 
	 @Bean
	 public WorkerSkillRepository workerSkillRepository() {
		 return new CoreWorkerSkillRepository(databaseRepo);
	 }
	 
	 @Value("${site.review.dir}") String reviewDirector;
	 @Value("${site.review.dir.archived}") String reviewArchDirectory;
	 
	 @Bean
	 public SiteReviewsRepository siteReviewsRepository() {
		 return new CoreSiteReviewsRepository(reviewDirector, reviewArchDirectory);
	 }
	 */
	 
	/* @Bean
	   public Docket productApi() {
	      return new Docket(DocumentationType.SWAGGER_2).select()
	         .apis(RequestHandlerSelectors.basePackage("com.helpfinder.")).build();
	   }
	*/
	@Value("${db.driverClassName}") String dbDriverClass;
	@Value("${db.url}") String dbUrl;
	 @Bean
	 public DataSource dataSource() {
	     final DriverManagerDataSource dataSource = new DriverManagerDataSource();
	     dataSource.setDriverClassName(dbDriverClass);
	     dataSource.setUrl(dbUrl);	   
	     return dataSource;
	 }
}
