/*
 * BU Term project for cs622
 
  This class will use openstreet map apis to expose functionality related to users location
  // the code reference is taken from http://julien.gunnm.org/geek/programming/2015/09/13/how-to-get-geocoding-information-in-java-without-google-maps-api/
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;

//This class user open street map to expose location related features
@Component
public class OpenstreetMapLocationRepository implements LocationServiceRepository {
	
	
	final String openStreetMapurl = "https://nominatim.openstreetmap.org/search?q=";
    /***
     * gets the lat log of a given address
     * 
     * @param address string address
     * @return Double[] lat and long of the given address
     */
    @Override
    public Double[] getLatLogFromAddress(String address) {
    	Double[] res = new Double[2];
        StringBuffer query;
        //split using space or ,coma        
        String[] split = address.split( "[\\s,]");
        String queryResult = null;
        query = new StringBuffer();
        // set the url to call
        query.append(openStreetMapurl);

        if (split.length == 0) {
            return null;
        }

        // construct the query address to search
        for (int i = 0; i < split.length; i++) {
            query.append(split[i]);
            if (i < (split.length - 1)) {
                query.append("+");
            }
        }
        // append the address with the url
        query.append("&format=json&addressdetails=1");
        try {
        	// call the api
        	queryResult = makeOpenStreetMapApiRequest(query.toString());
        }catch(IOException ex) {
        	System.out.println("Error calling openstreetmap api" + ex.getMessage());        	
        }

        if (queryResult == null)            
        	return null;

        // convert to json object
        Object obj = JSONValue.parse(queryResult);   

        // parse the object array 
        if (obj instanceof JSONArray) {
            JSONArray array = (JSONArray) obj;
            if (array.size() > 0) {
                JSONObject jsonObject = (JSONObject) array.get(0);

                // get the lat and log from json object
                String lon = (String) jsonObject.get("lon");
                String lat = (String) jsonObject.get("lat");
                // set the values in response object
                res[0] = Double.parseDouble(lat);
                res[1] = Double.parseDouble(lon);

            }
        }

        return res;
    }

    /***
     * checks if an address is valid
     * 
     * @param address   the input address     * 
     * @return boolean if true if valid address else false
     */
    @Override
    public boolean isValidAddress(String address) {
    	Double[] latLong = getLatLogFromAddress(address);
    	return latLong != null && latLong.length == 2 && latLong[0] != null && latLong[1] != null;
    }
    

    // makes an api request to opstreet map api
    private String makeOpenStreetMapApiRequest(String url) throws IOException {

        final URL obj = new URL(url);
        // call the url
        final HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        con.setRequestMethod("GET");

        if (con.getResponseCode() != 200 && con.getResponseCode() != 301) {
            return null;
        }

        try(BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()))){
	        String inputLine;
	        StringBuffer response = new StringBuffer();
	
	        while ((inputLine = in.readLine()) != null) {
	            response.append(inputLine);
	        }
	        return response.toString();
        }
        catch(Exception ex)        {
        	return "";
        }
        
    }

}
