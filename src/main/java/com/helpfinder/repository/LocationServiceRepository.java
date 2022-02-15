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
    public double[] getLatLogFromAddress(String address);

    // checks if the address is valid
    public boolean isValidAddress(String address);

}
