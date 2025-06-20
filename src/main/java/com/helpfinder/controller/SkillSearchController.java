/* 
 
 This class is used for exposing the apis required for searching user skills   
 * @author  Harish Janardhanan * 
 * @since   10-Feb-2022
 */

package com.helpfinder.controller;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.collections4.MultiValuedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.request.SkillSearchQueryRequest;
import com.helpfinder.model.response.SrchResultUser;
import com.helpfinder.service.WorkforceLocatorService;

// This class is used for exposing the apis required for searching for worker skills  
//This is not implementation completely
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/skill")
public class SkillSearchController {
 
    @Autowired
    WorkforceLocatorService<BasicUser> workforceLocatorService;
  
     /***
     * search for skills 
     * @param SkillSearchQueryRequest
     * @return 
     */
    @PostMapping("/search")
    public ResponseEntity<?> search(@Valid @RequestBody SkillSearchQueryRequest query) {
        try{
            if(query.getSearchQuery() == null || query.getSearchQuery().length() < 3)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Skills search should be more than 3 chars long");
            if(query.getMileRadius() < 1)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Search miles should be minimum 1 mile radius");
            if(query.getUserLat() == 00.d || query.getUserLong() == 00.d)
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Lat long provided is invalid");
            
            List<String> skills = new ArrayList<String>(Arrays.asList(query.getSearchQuery().split("[, +]")));
            double[] latLong = new double[2];
            latLong[0] = query.getUserLat();
            latLong[1] = query.getUserLong();
            ArrayList<SrchResultUser> resultUser = new ArrayList<>();
            MultiValuedMap<Double, BasicUser>  result = workforceLocatorService.findWorkforce(latLong, skills, query.getMileRadius());
            
            //make the response object
            for(double key : result.asMap().keySet()) {
                for(BasicUser user : result.get(key)) {
                    resultUser.add(new SrchResultUser(key, user));
                }
            }
            return ResponseEntity.ok(resultUser);
        }
        catch(Exception ex){
            System.err.println("Invalid UserName or Password " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message" ,"No Search results"));
        }
    }
}
