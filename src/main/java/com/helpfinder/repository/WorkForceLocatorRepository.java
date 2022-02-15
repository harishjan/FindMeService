/*
 * BU Term project for cs622
 
  This interface is the implementation for the data access layer to detect the works  based on different criterias
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import java.util.List;

import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.stereotype.Component;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.WorkInquiry;

@Component
public interface WorkForceLocatorRepository<T extends BasicUser> {

    // find and return the users matching the skills and mileRadius
       //returns multivaluemap of distance and the user
    public MultiValuedMap<Double, T> findWorkforceForSkills(double[] latlong, List<String> skills, double mileRadius);
    
    // get the inquiry for the given inquiryId
    public WorkInquiry getInquiry(int inquiryId);
    
    /***
     * converts the arccosine distance to kilometers
     * @param distanceInACos
     * @return double the kilometers value 
     */
    public static double convertCosToKiloMeters(double distanceInACos) {    
        return Math.acos(distanceInACos) * 6380; 
    }
    
    /***
     * convert arccosine distance to miles
     * @param distanceInACos
     * @return double the value in miles
     */
    public static double convertCosToMiles(double distanceInACos) {
           return Math.acos(distanceInACos ) *  (double)3958.756;
    }
    

    /***
     * convert the given value to radians cosine value
     * @param radians
     * @return double the cosine value
     */
    public static double getCosRadians(double value) {
           return Math.cos(degree2radians(value));
    }
    
    ///formula to convert miles to cos
    public static double getDistanceInMilesToCos(double distanceinMile) {        
        return Math.cos(distanceinMile / 3958.756);        
    }
 
    
    /***
     * convert the given value  raidans sine value
     * @param radians
     * @return double the sine value
     */
    public static double getSinRadians(double value) {
           return Math.sin(degree2radians(value));
    }
    
    /**
     * converts degree to radians
     * @param deg the value in degrees which needs conversion
     * @return double the value in radians
     */
    public static double degree2radians(double degree) {
        return (degree * Math.PI / 180.0);
    }
    
}

