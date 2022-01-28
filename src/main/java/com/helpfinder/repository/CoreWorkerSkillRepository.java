/*
 * BU Term project for cs622
 
  This class implements methods used to manage work skills in the system
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.helpfinder.model.WorkerSkill;

// not implemented completely
@Component
public class CoreWorkerSkillRepository implements WorkerSkillRepository {

    @Autowired
    DatabaseRepository databaseRepository;
    List<WorkerSkill> skills;
    
    /**
     * constructor     * 
     */    
    public CoreWorkerSkillRepository(DatabaseRepository databaseRepository) {
        this.databaseRepository = databaseRepository;
        // hard coded skills
        this.skills = new ArrayList<WorkerSkill>();
        this.skills.add(new WorkerSkill(1, "Handy Man"));
        this.skills.add(new WorkerSkill(2, "Painting"));
        this.skills.add(new WorkerSkill(3, "Cooking"));
        this.skills.add(new WorkerSkill(4, "House cleaning"));
    }
        
    
    /**
     * gets the list of skills which are available in the system to hire
     * 
     * @return List<WorkerSkill> List of skills available in the system to hire
     */
    @Override
    public List<WorkerSkill> getAllSkillsets() {
        return skills;
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
