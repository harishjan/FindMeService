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
               double[] latLong = locationRepository.getLatLogFromAddress("1600 Pennsylvania Avenue NW, Washington, DC 20500");
               
        }
}
