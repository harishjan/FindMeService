package com.helpfinder.model.response;

import com.helpfinder.model.BasicUser;

// response object for the skill search result
public class SrchResultUser {
    private double distanceFoundAwayfrom;
    private BasicUser user;
    public SrchResultUser(double distanceFoundAwayfrom, BasicUser user) {
        this.distanceFoundAwayfrom = distanceFoundAwayfrom;
        this.user = user; 
    }
    
    public void setDistanceFoundAwayfrom(double distanceFoundAwayfrom) {
        this.distanceFoundAwayfrom = distanceFoundAwayfrom;
    }
    
    public double getDistanceFoundAwayfrom() {
        return this.distanceFoundAwayfrom;
    }
    
    public void setUser(BasicUser user) {
        this.user = user;
    }
    
    public BasicUser getUser() {
        return this.user;
    }

}
