/* 
 
  This class the different skills used in the system which can be associated with a WorkUser
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import java.io.Serializable;

public class WorkerSkill implements Serializable {
    /**
        * 
        */
    private static final long serialVersionUID = 1L;
       // id reference of the work skill
    private int workerSkillId;
    //the name of the skill
    private String workerSkillName;
    
    /**
     * constructor
     * @param workSkillId id reference of the work skill 
     * @param workSkillName the name of the skill    
     */
    public WorkerSkill(int workerSkillId, String workerSkillName)
    {
        this.workerSkillId = workerSkillId;
        this.workerSkillName = workerSkillName;
        
    }
    
    /**
     * gets the id of the work skill     *     
     */
    public int getWorkSkillId()    {
        return this.workerSkillId;
    }
    
    /**
     * sets the id of the work skill     *     
     */
    public void setWorkSkillId(int workerSkillId)    {
        this.workerSkillId = workerSkillId;
    }
    /**
     * gets the name of the work skill     *     
     */
    public String getSkillName()    {
        return this.workerSkillName;
    }
    
    /**
     * sets the name of the work skill     *     
     */
    public void setSkillName(String workerSkillName)    {
        this.workerSkillName = workerSkillName;
    }
}
