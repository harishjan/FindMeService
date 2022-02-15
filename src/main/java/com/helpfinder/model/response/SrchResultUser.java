package com.helpfinder.model.response;

import com.helpfinder.model.BasicUser;

// response object for the skill search result
public class SrchResultUser {
    private double distanceFromUsersLocation;
    private BasicUser user;
    public SrchResultUser(double distanceFromUsersLocation, BasicUser user) {
        this.distanceFromUsersLocation = distanceFromUsersLocation;
        this.user = user; 
    }
    
    public void setDistanceFromUsersLocation(double distanceFromUsersLocation) {
        this.distanceFromUsersLocation = distanceFromUsersLocation;
    }
    
    public double getDistanceFromUsersLocation() {
        return this.distanceFromUsersLocation;
    }
    
    public void setUser(BasicUser user) {
        this.user = user;
    }
    
    public BasicUser getUser() {
        return this.user;
    }

}
