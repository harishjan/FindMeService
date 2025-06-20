package com.helpfinder.repository;

import java.util.List;

import com.helpfinder.model.SearchPreference;
import com.helpfinder.model.WorkerSkill;

//this interface defines the functionalites of search preference repo
public interface SearchPreferenceRepo<T extends SearchPreference> {
    
    public List<T> getSearchPreference(long userId);
    public void addSearchPreference(T searchPreference);
    public List<T> getAllSearchPreferenceBySkill(WorkerSkill skill);    
    public List<T> deleteSearchPreferenceBySkill(long userId, WorkerSkill skill);
    public List<T> UpdateSearchPreferenceBySkill(long userId, WorkerSkill skill);
    

}
