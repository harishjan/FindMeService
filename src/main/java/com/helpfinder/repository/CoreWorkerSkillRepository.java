/*
 * BU Term project for cs622
 
  This class implements methods used to manage work skills in the system
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helpfinder.model.WorkerSkill;

// not implemented completely
@Component
public class CoreWorkerSkillRepository implements WorkerSkillRepository {

    @Autowired
    DatabaseRepository databaseRepository;
    HashMap<Integer, WorkerSkill> skills;
    private String selectWorkerSkill = "select WorkerSkillid, workerSkillName from WorkerSkill;";
  
    /**
     * constructor     * 
     */    
    public CoreWorkerSkillRepository(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
        loadWorkerSkills();
    }
    
    private void loadWorkerSkills()    {
        try {
            this.skills =  (HashMap<Integer, WorkerSkill>)databaseRepository.executeSelectQuery(selectWorkerSkill, (s)->{},(result)->{
                HashMap<Integer, WorkerSkill> skills = new HashMap<Integer, WorkerSkill>();
                try {
                   while(result != null && result.next()) {
                       skills.put((Integer)result.getInt("WorkerSkillid"), new WorkerSkill(result.getInt("WorkerSkillid"), 
                       result.getString("workerSkillName")));
                   }            
                } catch (SQLException e) {                       
                        System.err.println("Error fetching work skills");
                }
                return skills;
            });
        } catch (SQLException e) {
            System.err.println("Error fetching work skills");
        }                
        
    }
    
    /***
     * gets the work skill matching the work skill id
     */
    @Override
    public WorkerSkill getWorkerskillById(int id) {
        if(skills== null || skills.size() == 0)
            loadWorkerSkills();
        return skills.get(id);
    }
    
    /**
     * gets the work skill matching the skill name
     */
    @Override
    public WorkerSkill getWorkerskillByName(String skillName) {
        if(skills== null || skills.size() == 0)
            loadWorkerSkills();
        List<WorkerSkill> skill = skills.values().stream().filter(x -> x.getSkillName().toLowerCase()
                .equals(skillName.toLowerCase())).collect(Collectors.toList());
        if(skill.size() > 0)
            return skill.get(0);
        return null;
    }
    
    /**
     * gets the list of skills which are available in the system to hire
     * 
     * @return List<WorkerSkill> List of skills available in the system to hire
     */
    @Override
    public List<WorkerSkill> getAllSkillsets() {
        if(skills== null || skills.size() == 0)
            loadWorkerSkills();
        return new ArrayList<WorkerSkill>(skills.values());
    }

    @Override
    public void captureNewSkillRequest(Long userId, List<WorkerSkill> skill) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public List<WorkerSkill> getUnreviewedSkillRequest(List<WorkerSkill> skill) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<WorkerSkill> addNewSkill(List<WorkerSkill> skill) {
        // TODO Auto-generated method stub
        return null;
    }
    

}
