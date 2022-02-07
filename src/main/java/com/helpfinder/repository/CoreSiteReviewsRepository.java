/*
 * BU Term project for cs622
 
  This class implements methods used manage site reviews
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */

package com.helpfinder.repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.commons.io.comparator.LastModifiedFileComparator;

import com.helpfinder.common.DateFormatter;
import com.helpfinder.exception.InvalidSiteReviewException;
import com.helpfinder.model.SiteReview;

@Component
//This class implements methods used manage site reviews
public class CoreSiteReviewsRepository implements SiteReviewsRepository{
    
   //directory where the site review files will be added     
   @Value("${site.review.dir}") public String reviewDirectory;
	//file where archived files will be added     
   @Value("${site.review.dir.archived}") public String reviewArchDirectory;
	 //save file in serialized object configuration to enable it
   @Value("${site.review.saveasserializedobject}")boolean saveAsSerializedObject;
   
   public CoreSiteReviewsRepository(@Value("${site.review.dir}")String reviewDirectory,
                        @Value("${site.review.dir}")String reviewArchDirectory, @Value("${site.review.saveasserializedobject}")boolean saveAsSerializedObject) {
        this.reviewDirectory = reviewDirectory;
        this.reviewArchDirectory = reviewArchDirectory;        
        this.saveAsSerializedObject = saveAsSerializedObject;
    }
    
    /***
     * Saves the review in a file
     * @param review the review object which should be saved
     */
    @Override
    public void saveSiteReview(SiteReview review) throws InvalidSiteReviewException{
    	if(saveAsSerializedObject)
    		saveAsSerializedObject(review, reviewDirectory);
    	else
    		saveAsString(review);
    }  
    
    /***
     * save the review as a object
     * @param review
     * @param destination
     * @throws InvalidSiteReviewException
     */
    private void saveAsSerializedObject(SiteReview review, String destination) throws InvalidSiteReviewException {
    	//fileId is the reference to fetch this specific review file      
        String fileId = createFile(review, destination);
        review.setReviewId(fileId);
    	try(ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileId))){
    		output.writeObject(review);
    	}
    	catch(IOException ex) {
    		 System.err.println("Error opening the file " + ex.getMessage());
             throw new InvalidSiteReviewException(ex.getMessage());    
    	}
    }
    
    
    //constructs a file name and creates a the file and will return the file full path
    private String createFile(SiteReview review, String destination) throws InvalidSiteReviewException {
    	//create a new file with user_guid.txt format
    	 String fileName = review.getUserId() + "_"+ java.util.UUID.randomUUID().toString() + ".txt";
    	  //fileId is the reference to fetch this specific review file      
         String fileId = createIfnotExist(destination, fileName);
         return fileId;
    }
    
    //save the review as string 
    private void saveAsString(SiteReview review) throws InvalidSiteReviewException {
         
        //fileId is the reference to fetch this specific review file      
        String fileId = createFile( review, reviewDirectory);
        review.setReviewId(fileId);
         // open file
        try(Formatter outfile = new Formatter(fileId)) {
              outfile.format(review.toString());              
         }
         catch (FileNotFoundException ex) {
             System.err.println("Error opening the file " + ex.getMessage());
             throw new InvalidSiteReviewException(ex.getMessage());    
         }           
    }
    
   
    /**
    *creates a file if does not exist for the given file path
    *@param filePath the file path 
    *@param fileName the file name
    *@return String the full file path 
     * @throws InvalidSiteReviewException 
    */
    private String createIfnotExist(String filePath, String fileName) throws InvalidSiteReviewException {    
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
                throw new InvalidSiteReviewException(e.getMessage());    
            }
        }
        
        return file.getPath();
    }
    
    //Converts a string from the input file path which has string representation of the object to SiteReview object
    private SiteReview convertToReviewFromString(String filePath) throws IOException, InvalidSiteReviewException{
    	 SiteReview review = null;
    	 //create a buffer reader
        try(BufferedReader reader = new BufferedReader(new FileReader(filePath))) {            
	        // this variable keeps track of lines from where the text is extracted to construct the SiteReview object
	        int line = 0;// set the line number in file
	        String curLine = reader.readLine();
	        review = new SiteReview();
	        while (curLine != null) {
	            // pick the values from line 1,3,5,7,9, 11 and parse and set in SiteReview object
	            switch (line) {
	                case 1 : 
	                         review.setUserId(Long.parseLong(curLine));
	                         break;
	                case 3: 
	                         review.setTitle(curLine);
	                         break;
	                case 5:
	                         review.setComment(curLine);
	                         break;
	                case 7: review.setSubmittedDate(DateFormatter.convertToSystemDate(curLine));
	                         break;
	                 case 9:
	                         review.setReviewId(curLine);
	                         break;
	                 case 11:
	                	 if(curLine != null || curLine.length() != 0)
	                		 review.setReviewedDate(DateFormatter.convertToSystemDate(curLine));
                         break;
	                 default: break;
	            }
	            
	           //get the next line
	            curLine = reader.readLine();
	            //increment the line
	            line++;                    
	        }
        }catch(ParseException ex){
        	throw new InvalidSiteReviewException(ex.getMessage());
        }

    
        return review;
    }
    
    
    /***
     * Converts a string from the input file path which has string representation of the object to SiteReview object 
     * @param filePath
     * @return SiteReview the instance deserialized form file 
     * @throws IOException
     * @throws InvalidSiteReviewException
     */
    private SiteReview convertReviewFromStreamToObject(String filePath) throws IOException, InvalidSiteReviewException{
    	
    	try(ObjectInputStream input = new ObjectInputStream(new FileInputStream(filePath))) {
    		
    		return (SiteReview)input.readObject();
    		
    	} catch (FileNotFoundException | ClassNotFoundException ex) {
            System.err.println("Error opening the file " + ex.getMessage());
            throw new InvalidSiteReviewException(ex.getMessage());    
        }   
    }

    /**
     * gets a list of reviews up to the limit sorted by date descending of the file created
     * @param limit the number of reviews to return
     * @param sortDateDesc boolean value indicating if the reviews fetched are to be sorted descending based on reviewed date  
     * @throws InvalidSiteReviewException 
       
     */
    @Override
    public List<SiteReview> getSiteReview(int limit, boolean sortDateDesc) throws InvalidSiteReviewException {
        
        // list of reviews to return
        List<SiteReview> reviews = new ArrayList<SiteReview>();
        try {
            //get the directly with the files
            File directory = new File(FileSystems.getDefault().getPath("").toAbsolutePath()+ File.separator+ reviewDirectory);
            // get the file list
            File[] files = directory.listFiles();
            if(files == null || files.length == 0)
                return reviews;
            // sort based on last modified date
            Arrays.sort(files, sortDateDesc ?  LastModifiedFileComparator.LASTMODIFIED_COMPARATOR : LastModifiedFileComparator.LASTMODIFIED_REVERSE);
            //SiteRevie list to return
           
            // loop through all files
            for (File file : files) {
                if(!file.isFile())
                    continue;
                // stop processing the file if the limit asked for has reached
                if(limit == 0)
                    break;
                SiteReview review = saveAsSerializedObject? convertReviewFromStreamToObject(file.getPath()) : convertToReviewFromString(file.getPath());
                 //add to list
                reviews.add(review );
                       
                  // decrease the limit
                 limit--;         
            }
        }
        catch(IOException ex) {
             System.err.println("Error fetching files " + ex.getMessage());
             throw new InvalidSiteReviewException(ex.getMessage());
        }
        return reviews;
    }

    //archive the review as string
    private void archivedReviewAsString(Path source) throws InvalidSiteReviewException, IOException    {    	
    	 String destinationFilePath = createIfnotExist(reviewArchDirectory , source.getFileName().toString());
         Path destination = Paths.get(destinationFilePath);
    	// move the file
        Files.move(source, destination , StandardCopyOption.REPLACE_EXISTING);
        // append in the file the reviewed date
        try(Formatter outfile = new Formatter(new FileWriter(destinationFilePath, true))){ // open file        
            
        	outfile.format(String.format("\nReviewedDate\n%s", DateFormatter.convertToSystemDateNowString()));
        }
    	
    }
    
    //archive the review as stream
    private void createArchivedReviewAsStream(Path source) throws IOException, InvalidSiteReviewException    {
    	// get the deserialized object
    	SiteReview review = convertReviewFromStreamToObject(source.toString());
    	// save this object in arch folder
    	saveAsSerializedObject(review, reviewArchDirectory);
    	// delte the source
    	Files.delete(source);
    }
    /**
     * Moves the review to archived folder
     * @param id the source file name with the full path which should be archived
     * @throws InvalidSiteReviewException 
     * @throws IOException 
     */
    @Override
    public void archiveReview(String reviewId) throws InvalidSiteReviewException {    
        
        try {
            // create source and destination path
            Path source = Paths.get(reviewId);
            File sourceFile = new File(reviewId);
            if(!sourceFile.exists() || !sourceFile.isFile())
                throw new InvalidSiteReviewException("Review Id is Invalid");
                                        
            if(saveAsSerializedObject)
            	createArchivedReviewAsStream(source);
            
            else archivedReviewAsString(source);
        }
        catch(IOException ex) {
            System.err.println("Error archiving the files " + ex.getMessage());
            throw new InvalidSiteReviewException(ex.getMessage());
        }
            
    }

}

