package com.helpfinder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


//not implemented
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@ComponentScan()

public class HelpfinderApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelpfinderApplication.class, args);
	}
	
	
	 
	 
	 
}
