package com.helpfinder.repository;



import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

@TestInstance(Lifecycle.PER_CLASS)
//Test class to verify location services are working
//still working on this implementation
public class OpenstreetMapLocationRepositoryTest {
	
	OpenstreetMapLocationRepository locationRepository;
	 @BeforeAll
	  public void setup()    {
		 locationRepository = new OpenstreetMapLocationRepository();    
	  }
	 @Test
	 public void GetLatLongTest()
	 {
		 Double[] latLong = locationRepository.getLatLogFromAddress("101 mount pleasant ave, Edison, nj, 08820");
		 
	 }
}
