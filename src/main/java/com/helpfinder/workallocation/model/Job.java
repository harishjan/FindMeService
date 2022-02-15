package com.helpfinder.workallocation.model;

// this is the class which defines the job which a worker does
public class Job {
	
	// the job Id
	private int jobId;
	// the job name
	private String jobName;
	// the complexity of the job fom 1- 100
	private int complexity;
	
	public void setJobId(int jobId) {
		this.jobId = jobId;		
	}
	public int getJobId() {
		return this.jobId;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;		
	}
	
	public String getJobName() {
		return this.jobName;
	}
	
	public void setComplexity(int complexity) {
		this.complexity = complexity;
	}
	
	public int getComplexity() {
		return this.complexity;
	}
	

}
