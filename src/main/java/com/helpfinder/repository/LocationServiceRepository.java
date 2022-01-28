/*
 * BU Term project for cs622
 
  This interface is the implementation for the data access layer to location specific functionalities
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import org.springframework.stereotype.Component;

@Component
public interface LocationServiceRepository {
    // gets the latLog From a given address
    public Double[] getLatLogFromAddress(String address);

    // gets the distance between two lat longs
    public double getDistanceBetweenLatLong(Double[] source, Double[] destination);

}
