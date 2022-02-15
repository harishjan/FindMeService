package com.helpfinder.workallocation;
import java.util.function.Consumer;

import com.helpfinder.workallocation.model.Job;
import com.helpfinder.workallocation.model.Worker;

// this is the work done by any of the work and implements Runnable
public class Work implements Runnable  {
	
	private com.helpfinder.workallocation.model.Job job;
	private com.helpfinder.workallocation.model.Worker worker;
	// the call back function  to be triggered when job is completed
	private Consumer<Work> OnJobComplete;
	/**
	 * 
	 * @param job the job that is assigned to a worker
	 * @param worker to this the job is assigned
	 */
	public Work(Job job, Worker worker, Consumer<Work> OnJobComplete) {
		
		this.job = job;
		this.worker = worker;
		this.OnJobComplete =  OnJobComplete;
		
	}
	/***
	 * Calculates the time taken for the worker to finish the job
	 * if C is the complexity of work and the E Efficiency of the worker, 
		Then the amount of time to complete a work is calculated as : C / E
		For Example:
		For a work with complexity C = 100 and Efficiency E = 10 -> will take C/E = 100/10 * 1000 ms = 10000 sec to complete
		For a work with complexity C = 100 and Efficiency E = 5 -> will take C/E = 100/5 * 1000 ms= 50000 sec to complete
		For a work with complexity C = 70 and Efficiency E = 5 -> will take C/E = 70/5 * 1000 ms= 25000 sec to complete

	 * @return the time to complete in ms
	 */

	public long getTotalTimeToCompleteJob()	{
		// calculate the time required to do the job
		//For a work with complexity C = 100 and 
		//Efficiency E = 10 -> will take C/E = 100/10 * 1000 ms = 10000 Ms to complete
		long jobComplexity = job.getComplexity();
		long workerEfficiency = worker.getEfficiency();
		// calculate total time taken to finish Job in Ms
		return (jobComplexity / workerEfficiency) * 1000;
	}
	
	// this is the implementation of the work that is being done by a worker 
	@Override
	public void run() 
    {	
		try {
			// run the job for total time taken to finish Job in Ms
			Thread.sleep(getTotalTimeToCompleteJob());
			// inform the job is completed
			this.OnJobComplete.accept(this);
		} catch (InterruptedException e) {
			
		}
		
		
    } 
	
	// get the worked assigned to this work
	public Worker getWorker()	{
		return this.worker;
	}
	
	//get job assigned to this work
	public Job getJob()	{
		return this.job;
	}

}
