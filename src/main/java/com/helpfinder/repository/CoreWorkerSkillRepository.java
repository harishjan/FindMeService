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
        this.skills.add(new WorkerSkill(1, "HandyMan"));
        this.skills.add(new WorkerSkill(2, "Painter"));
        this.skills.add(new WorkerSkill(3, "Cooking"));
        this.skills.add(new WorkerSkill(4, "Housecleaning"));
        this.skills.add(new WorkerSkill(5, "Maintenance"));
        this.skills.add(new WorkerSkill(6, "Plumber"));
        this.skills.add(new WorkerSkill(7, "Electrician"));
        this.skills.add(new WorkerSkill(8, "Carpenter"));
        this.skills.add(new WorkerSkill(9, "SnowRemoval"));
        this.skills.add(new WorkerSkill(10, "Demolition"));
        this.skills.add(new WorkerSkill(11, "DebrisRemoval"));
        this.skills.add(new WorkerSkill(12, "UnloadingAndLoading"));
        this.skills.add(new WorkerSkill(13, "ForkliftOperation"));
        this.skills.add(new WorkerSkill(14, "HandToolsPowerTools"));
        this.skills.add(new WorkerSkill(15, "FarmAndFieldWork"));
        this.skills.add(new WorkerSkill(16, "Catering"));
        this.skills.add(new WorkerSkill(17, "GrassCutting"));
        this.skills.add(new WorkerSkill(18, "Construction"));
        this.skills.add(new WorkerSkill(19, "SecurityGuard"));
        this.skills.add(new WorkerSkill(20, "Chauffeur"));
        this.skills.add(new WorkerSkill(21, "Butler"));
        this.skills.add(new WorkerSkill(22, "Janitor"));
        this.skills.add(new WorkerSkill(23, "AthleticTrainer"));
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
