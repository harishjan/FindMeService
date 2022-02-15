package com.helpfinder.workallocation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.helpfinder.workallocation.model.Job;
import com.helpfinder.workallocation.model.Worker;

/**
 * this class fetches the workers and jobs and exposes the method required to process the jobs with the workers available
 * @author Harish Janardhanan
 *
 */
public class JobAllocationProcess {
	// using ConcurrentLinkedQueue to make the queue thread safe 
	private Queue<Worker> workers = new ConcurrentLinkedQueue<Worker>();	
	private Queue<Job> jobs = new ConcurrentLinkedQueue<Job>();
	private String jobFile;
	private String workerFile;
	// this is the lock object for job completed tracking
	private static Object lockObj = new Object();
	// the amount of money payed for based on complexity is factored by this value
	private double workComplexityMultiplier = 0.01; 
	//to store the work completed
	private List<Work> workCompleted = new ArrayList<>();
	
	/***
	 * Constructor
	 * @param jobFile the file where the job details are stored, the file should have following for each job in separate lines  
	 * jobid(integer ) JobName(String) Complexity(integer value ranging from 1 - 100)
	 * @param workerFile the file where work details are stored, the file should have following for each job in separate lines  
	 * workerid(integer) WorkerName(worker name) Efficiency(integer value range from 1- 100)
	 */
	public JobAllocationProcess(String jobFile, String workerFile)	{
		this.jobFile = jobFile;
		this.workerFile = workerFile;
		populateWorkerAvailable();
		populateJobs();		
	}
	
	//this method is called once the work is completed
	private void onJobComplete(Work work)
	{			
		//award the money for the work
		awardWorkerEarnings(work);
		addWorkCompleted(work);
		//make this worker free to make them available for the next job
		addWorker(work.getWorker());
		
	}
	
	//tracks the work completed
	private  synchronized void addWorkCompleted(Work work) {
		synchronized (lockObj) {
			workCompleted.add(work);
		}
	}
	
	
	public double getWorkComplexityMultiplier() {
	    return workComplexityMultiplier;
	}
	
	//calculates and awards the amount the work has earned for a work
	private void awardWorkerEarnings(Work work) {
		// multiply workComplexityMultiplier to find the amount to be paid
		work.getWorker().addEarning(work.getJob().getComplexity() * workComplexityMultiplier);
	}
	
	//adds the work to the queue to make it available for the next job
	private void addWorker(Worker worker) {
		workers.add(worker);
	}
	
	// fetches the works from the file
	private void populateWorkerAvailable(){
		  try(BufferedReader reader = new BufferedReader(new FileReader(workerFile))) {            
              // this variable keeps track of lines from where the text is extracted to construct 
              int line = 0;// set the line number in file
              String curLine = reader.readLine();//get the next line            
              while (curLine != null) {
            	  String[] array = curLine.split(" ");
            	  Worker worker = new Worker();
            	  worker.setWorkerId((int)Integer.valueOf(array[0]));
            	  worker.setWorkerName(array[1]);
            	  worker.setEfficiency((int)Integer.valueOf(array[2]));
            	  workers.add(worker);
            	  //get the next line
                  curLine = reader.readLine();
                  //increment the line
            	  line++;
              }
              System.out.println("Total number of workers found : " + line);
          
		}catch(IOException ex){
          System.out.println("Error processing worker file : " + ex.getMessage());
          
		}
	}
	
	//fetches the job information from the file
	private void populateJobs() {
		try(BufferedReader reader = new BufferedReader(new FileReader(jobFile))) {            
			// this variable keeps track of lines from where the text is extracted to construct 
			int line = 0;// set the line number in file
			String curLine = reader.readLine();//get the next line		
            while (curLine != null) {
				String[] array = curLine.split(" ");
				Job job = new Job();          	  
	          	job.setJobId((int)Integer.valueOf(array[0]));
	          	job.setJobName(array[1]);
	          	job.setComplexity((int)Integer.valueOf(array[2]));
	          	jobs.add(job);
	      	   //get the next line
	            curLine = reader.readLine();
	            //increment the line
	      	  	line++;
            }
            System.out.println("Total number of jobs found : " + line);
        
		}catch(IOException ex){
        System.out.println("Error processing job file : " + ex.getMessage());
        
		}
	}
	
	
	/***
	 * This method will allocate workers for the jobs to be completed.
	 * If all the workers are busy, then it will wait until a worker is available
	 */
	public void startProcessingJobs() 
	{
		ArrayList<Thread> workThreads = new ArrayList<Thread>();
		while(jobs.peek() != null) {
			// get the job to be done
			Job job = jobs.poll();
			
			// wait for a worker to be available
			while(workers.peek() == null) { }
			// if there are workers available
			// get a worker
			Worker worker = workers.poll();
			// create a thread and start the work
			Thread workThread = new Thread(new Work(job,worker, (completedWork)->{
			 //once job is completed 
			 onJobComplete(completedWork);
			}));
			//start the work
			workThread.start();
			//track the threads started
			workThreads.add(workThread);	    	
	    	 
	     }
	     //wait for the work to be completed
	     for (Thread work : workThreads)
			try {
				work.join();
			} catch (InterruptedException e) {
				// ignore this exception as we are waiting for the jobs to be completed
			}
	}
	
	//this returns the work completed, this is an unmodifiable list so that this list is not modified outside
	public List<Work> getWorkCompleted()	{
	    synchronized (lockObj) {
	        return Collections.unmodifiableList(workCompleted);
        }
	    
	}
	

}
