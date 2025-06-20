package com.helpfinder.model;

// this call represents the base implementation of a search preference
public abstract class SearchPreference {

    private WorkerSkill skill;
    private double mileRadius;
    private long userId;
    
    public void setSkill(WorkerSkill skill) {
        this.skill = skill;
    }
    
    public void setMileRadius(double mileRadius) {
        this.mileRadius = mileRadius;
    }
    
    public WorkerSkill getSkill() {
        return this.skill;
    }
    
    public double getMileRadius() {
        return this.mileRadius;
    }
    
    
    public void setUserId(long userId) {
        this.userId = userId;
    }
    
    public long getUserId() {
        return this.userId;
    }
}
