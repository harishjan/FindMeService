package com.helpfinder.workallocation;
/*
 
 
  Test class to verify joballocationprocessor
 * @author  Harish Janardhanan * 
 * @since   12-Feb-2022
 */


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.helpfinder.workallocation.model.Worker;



@TestInstance(Lifecycle.PER_CLASS)
//Test class to verify job allocation class
public class JobAllocationProcessTest {

    private JobAllocationProcess process;    
    /**
     *setup the comment logic for all test cases here 
     */
    @BeforeAll
    public void setup()    {
           // this instance stores the reviews in string format
        process = new JobAllocationProcess("./datafiles/jobs.txt","./datafiles/workers.txt");
    }
    
    /**
     * This test picks the files and test if all the jobs are processed
     */
    @Test
    public void test_AllTheJobsAreGettingProcessed()    {
        
        System.out.println("Assinging job to workers..... ");
        process.startProcessingJobs();                
        System.out.println("Jobs Finished, Verifying the details..... ");
        List<Work> workCompleted = process.getWorkCompleted();
        
        //check if all jobs are completed
        assertEquals(workCompleted.size(), 5);
        System.out.println(String.format("Total jobs completed %s : ", workCompleted.size()));
        //store the workers used so far
        List<Worker> workers = new ArrayList<>();        
        System.out.println("\n+++++++++++++++++++++++++++++++++++++++++++Job Details++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        for(Work work: workCompleted) {
            System.out.println(String.format("Job %s completed by worker %s and took %d ms to complete, and was awarded %.3f $",
                    work.getJob().getJobName(), work.getWorker().getWorkerName(), work.getTotalTimeToCompleteJob(),
                    (work.getJob().getComplexity() * process.getWorkComplexityMultiplier())));
            if(!workers.contains(work.getWorker()))
                workers.add(work.getWorker());
        }
        
        System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        
        System.out.println("\n+++++++++++++++++++++++++++++++++++++++++Worker Details+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        
        for(Worker worker : workers) {
            System.out.println(String.format("Worker %s earned %.3f $ in total ", worker.getWorkerName(), worker.getEarning()));        
        }
        System.out.println("\n++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++\n");
        
        
       
    }
    

}
