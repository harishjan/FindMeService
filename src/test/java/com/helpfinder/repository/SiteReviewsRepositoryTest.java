/*
 
 
  Test class to verify SiteReviewsRepository
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.helpfinder.exception.InvalidSiteReviewException;
import com.helpfinder.model.SiteReview;

@TestInstance(Lifecycle.PER_CLASS)
//Test class to verify SiteReviewsRepository
public class SiteReviewsRepositoryTest {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSS");

    private SiteReviewsRepository siteReviewsRepositoryString;
    private SiteReviewsRepository siteReviewsRepositoryStream;
    /**
     *setup the comment logic for all test cases here 
     */
    @BeforeAll
    public void setup()    {
           // this instance stores the reviews in string format
        siteReviewsRepositoryString = new CoreSiteReviewsRepository("/test/reviews/", "/test/reviews/arch/", false);
        // this instance stores the review in serialized format 
        siteReviewsRepositoryStream = new CoreSiteReviewsRepository("/test/reviewsstream/", "/test/reviewsstream/arch/", true);
    }
    
    
    
    /**
     * This test check if a review is saved in the file as stream
     */
    @Test
    public void test_Create_SiteReview_AsStream_and_check_StreamisSerialized_Properly()    {
        //store the data to verify later
        LocalDateTime now = LocalDateTime.now();
        String title  = "big title"+ dtf.format(now);
        //create review
        SiteReview review = new SiteReview();
        review.setUserId(1);
        review.setComment("a big bold comment");
        review.setSubmittedDate(java.sql.Date.valueOf(LocalDate.now()));
        review.setTitle(title);
        System.out.println("Creating review with below details ");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(review.toString());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
        //save the review
        try {
               siteReviewsRepositoryStream.saveSiteReview(review);
               System.out.println("Review serialized and stored successfully");
        } catch (InvalidSiteReviewException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //get the review here the sorting is done ascending to get the latest docs first
        List<SiteReview> siteReviews;
        try {
               //This should deserialize the object
            siteReviews = siteReviewsRepositoryStream.getSiteReview(10, false);
        
            boolean foundReview = false; 
            for(SiteReview siteReview : siteReviews) {
                if(siteReview.getTitle().equals(title)) {
                    foundReview  = true;
                    System.out.println("Review deserialized and pulled from file with below details");
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
                    System.out.println(siteReview.toString());
                    System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
                    break;
                }
            }
            assertTrue(foundReview);
            
        } catch (InvalidSiteReviewException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    

    /**
     * This test check if a review stored as stream archived/removed once archived*/
    @Test
    public void test_Create_SiteReviewStream_Archive_And_Check_if_Review_Exist()    {
        //store the data to verify later        
           
        LocalDateTime now = LocalDateTime.now();
        String title  = "big title"+ dtf.format(now);
        //create review
        SiteReview review = new SiteReview();
        review.setUserId(1);
        review.setComment("a big bold comment");
        review.setSubmittedDate(new Date());
        review.setTitle(title);
        System.out.println("Creating review with below details ");
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
        System.out.println(review.toString());
        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
        //save the review
        try {
               siteReviewsRepositoryStream.saveSiteReview(review);
               System.out.println("Review serialized and stored successfully");
        } catch (InvalidSiteReviewException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //Find from the list the file to be archived
        List<SiteReview> siteReviews;
        try {
            siteReviews = siteReviewsRepositoryStream.getSiteReview(10, false);
             
            for(SiteReview siteReview : siteReviews) {
                if(siteReview.getTitle().equals(title)) {
                    // archive the file using the review Id
                    try {
                           System.out.println("Review deserialized and pulled from file with below details");
                           System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
                        System.out.println(siteReview.toString());
                        System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++");
                           siteReviewsRepositoryStream.archiveReview(siteReview.getReviewId());
                           System.out.println("Review serialized and archived successfully");
                    } catch (InvalidSiteReviewException e) {                    
                        e.printStackTrace();
                    }
                    break;
                }
            }
        
            //Fetch the files again to check if the file exist        
            siteReviews = siteReviewsRepositoryStream.getSiteReview(10, false);
            boolean reviewDoesnotExist = true;
            
            for(SiteReview siteReview : siteReviews) {
                if(siteReview.getTitle().equals(title)) {
                    // set file found in which the test fails                
                    reviewDoesnotExist = false;
                    break;
                }
            }
            if(reviewDoesnotExist)                   
                   System.out.println(String.format("Review with title %s not found after archiving ", title));
            assertTrue(reviewDoesnotExist);
            
        } catch (InvalidSiteReviewException e1) {            
            e1.printStackTrace();
        }    
        
        
    }
    
    /**
     * This test check if a review is saved in the file
     */
    @Test
    public void test_Create_SiteRevie_check_The_review_saved()    {
        //store the data to verify later
        LocalDateTime now = LocalDateTime.now();
        String title  = "big title"+ dtf.format(now);
        //create review
        SiteReview review = new SiteReview();
        review.setUserId(1);
        review.setComment("a big bold comment");
        review.setSubmittedDate(java.sql.Date.valueOf(LocalDate.now()));
        review.setTitle(title);
        //save the review
        try {
               siteReviewsRepositoryString.saveSiteReview(review);
        } catch (InvalidSiteReviewException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //get the review here the sorting is done ascending to get the latest docs first
        List<SiteReview> siteReviews;
        try {
            siteReviews = siteReviewsRepositoryString.getSiteReview(10, false);
        
            boolean foundReview = false; 
            for(SiteReview siteReview : siteReviews) {
                if(siteReview.getTitle().equals(title)) {
                    foundReview  = true;
                    break;
                }
            }
            assertTrue(foundReview);
        } catch (InvalidSiteReviewException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        
    }
    
    
    /**
     * This test check if a review archived/removed once archived*/
    @Test
    public void test_Create_SiteReview_Archive_And_Check_if_Review_Exist()    {
        //store the data to verify later        
           
        LocalDateTime now = LocalDateTime.now();
        String title  = "big title"+ dtf.format(now);
        //create review
        SiteReview review = new SiteReview();
        review.setUserId(1);
        review.setComment("a big bold comment");
        review.setSubmittedDate(new Date());
        review.setTitle(title);
        //save the review
        try {
               siteReviewsRepositoryString.saveSiteReview(review);
        } catch (InvalidSiteReviewException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //Find from the list the file to be archived
        List<SiteReview> siteReviews;
        try {
            siteReviews = siteReviewsRepositoryString.getSiteReview(10, false);
             
            for(SiteReview siteReview : siteReviews) {
                if(siteReview.getTitle().equals(title)) {
                    // archive the file using the review Id
                    try {
                           siteReviewsRepositoryString.archiveReview(siteReview.getReviewId());
                    } catch (InvalidSiteReviewException e) {                    
                        e.printStackTrace();
                    }
                    break;
                }
            }
        
            //Fetch the files again to check if the file exist        
            siteReviews = siteReviewsRepositoryString.getSiteReview(10, false);
            boolean reviewDoesnotExist = true;
            
            for(SiteReview siteReview : siteReviews) {
                if(siteReview.getTitle().equals(title)) {
                    // set file found in which the test fails                
                    reviewDoesnotExist = false;
                    break;
                }
            }
            assertTrue(reviewDoesnotExist);
            
        } catch (InvalidSiteReviewException e1) {            
            e1.printStackTrace();
        }    
        
        
    }
    
    

}
