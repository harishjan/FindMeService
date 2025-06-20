package com.helpfinder.repository;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.util.List;

import com.helpfinder.exception.InvalidSearchPreferenceException;
import com.helpfinder.exception.InvalidSiteReviewException;
import com.helpfinder.model.SearchPreference;

import com.helpfinder.model.WorkerSkill;

// implementation of the site repo which is used to persist the search preference in a file
public class CoreSearchPreferenceRepo<T extends SearchPreference> implements SearchPreferenceRepo<T>{
 // this is the lock object for job completed tracking
    private static Object lockObj = new Object();
    @Override
    public synchronized List<T> getSearchPreference(long userId) {
        // TODO Auto-generated method stub
        return null;
    }
    
    // gets the stream from the file and deserialize the data to SearchPreference
    // code block is serialized
    private T convertSearchPreferenceFromStreamToObject(String filePath) throws IOException, InvalidSearchPreferenceException{
        synchronized (lockObj) {
            try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath))) {
                   
                   return (T)input.readObject();
                   
            } catch (FileNotFoundException | ClassNotFoundException ex) {
             System.err.println("Error opening the file " + ex.getMessage());
             throw new InvalidSearchPreferenceException(ex.getMessage());    
         }   
        }
    }
    
  
    private String createFile(T searchPreference, String destination) throws InvalidSearchPreferenceException {
        //create a new file with user_guid.txt format
         String fileName = searchPreference.getUserId() + "_"+ searchPreference.getSkill().getWorkSkillId() + ".txt";
          //fileId is the reference to fetch this specific review file      
      String fileId = createIfnotExist(destination, fileName);
      return fileId;
    }
    
    /**
     *creates a file if does not exist for the given file path
     *@param filePath the file path 
     *@param fileName the file name
     *@return String the full file path 
      * @throws InvalidSiteReviewException 
     */
     private String createIfnotExist(String filePath, String fileName) throws InvalidSearchPreferenceException {    
         //construct the directory path where the file needs to be created
         File file = new File(FileSystems.getDefault().getPath("").toAbsolutePath() + File.separator + filePath);
         //if directory does not exist then create it
         if(!file.exists()){
             file.mkdirs();
         }
         // create the file instance for the file path
         file = new File(file.getPath() + File.separator +fileName);
         // if file does not exist then create the file
         if(!file.exists()){          
             try {
                  file.createNewFile();
             } catch (IOException e) {
                 System.err.println("Error creating file " + e.getMessage());        
                 throw new InvalidSearchPreferenceException(e.getMessage());    
             }
         }
         
         return file.getPath();
     }
     
    private String pathToSave = "./searchPreference";
    //converts an 
    private void saveAsSerializedObject(T searchPreference, String destination) throws InvalidSearchPreferenceException {
        synchronized (lockObj) {
        //fileId is the reference to fetch this specific review file      
         String fileId = createFile(searchPreference, destination);         
            try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileId))){
                   output.writeObject(searchPreference);
            }
            catch(IOException ex) {
                    System.err.println("Error opening the file " + ex.getMessage());
              throw new InvalidSearchPreferenceException(ex.getMessage());    
            }
        }
    }
 

    @Override
    public void addSearchPreference(T searchPreference) {
        // TODO Auto-generated method stub
        
    }

    
    // get all the search Preferences, this is synchronized because when we fetch the skills there might be users adding or updating the skills 
    // this feature is admin to see the demand for different skills
    @Override
    public List<T> getAllSearchPreferenceBySkill(WorkerSkill skill) {
        synchronized (lockObj) {
            return null;
        }
    }

    @Override
    public List<T> deleteSearchPreferenceBySkill(long userId, WorkerSkill skill) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<T> UpdateSearchPreferenceBySkill(long userId, WorkerSkill skill) {
        // TODO Auto-generated method stub
        return null;
    }

}
