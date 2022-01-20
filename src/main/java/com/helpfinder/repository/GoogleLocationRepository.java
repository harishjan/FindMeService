/*
 * BU Term project for cs622
 
  This class will use google apis to expose functionality related to users location
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import org.springframework.stereotype.Repository;

@Repository
public class GoogleLocationRepository implements LocationServiceRepository {

	/**
	 * gets the lat log of a given address
	 * 
	 * @param address string address
	 * @return Double[] lat and long of the given addrss
	 */
	@Override
	public Double[] getLatLogFromAddress(String address) {
		// TODO hard coded
		Double[] latLong = new Double[2];
		latLong[0] = 1.1;
		latLong[1] = 2.2;
		return latLong;
	}

	/**
	 * gets the distance between to lat long points in miles
	 * 
	 * @param source      Double[] lat and log of the source
	 * @param destination Double[] lat and log of the destination
	 * @return double distance in miles
	 */
	@Override
	public double getDistanceBetweenLatLong(Double[] source, Double[] destination) {
		// TODO hardcoded
		return 1.8;
	}

}
