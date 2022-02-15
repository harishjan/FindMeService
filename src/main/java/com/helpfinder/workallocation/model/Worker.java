package com.helpfinder.workallocation.model;

//this class represent a worker
public class Worker {
	
	// this is the lock object
	private static Object lockObj = new Object(); 

	// the worker Id
	private int workerId;
	// the name of the user
	private String name;
	// efficiency 1 - 10
	private int efficiency;
	
	// Total money earned
	private double earning = 0.0;
	
	public void setWorkerId(int workerId) {
		this.workerId = workerId;		
	}
	public int getWorkerId() {
		return this.workerId;
	}
	
	public void setWorkerName(String name) {
		this.name = name;		
	}
	public String getWorkerName() {
		return this.name;
	}
	
	public void setEfficiency(int efficiency) {
		this.efficiency = efficiency;
	}
	public int getEfficiency() {
		return this.efficiency;
	}
	
	
	// award user additional earnings  
	public void addEarning(double earning) {
		synchronized (lockObj) {
			this.earning += earning;
		}
				
	}
	// Gets the total amount this worker has earned
	public double getEarning() {
		synchronized (lockObj) {
			return this.earning;
		}
	}

}
