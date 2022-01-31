/*
 * BU Term project for cs622
 
  Test class to verify SiteReviewsRepository
 * @author  Harish Janardhanan * 
 * @since   21-jan-2022
 */
package com.helpfinder.repository;

import static org.junit.Assert.assertTrue;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.helpfinder.exception.InvalidSiteReviewException;
import com.helpfinder.model.SiteReview;

@TestInstance(Lifecycle.PER_CLASS)
//Test class to verify SiteReviewsRepository
public class SiteReviewsRepositoryTest {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss.SSSS");

    private SiteReviewsRepository siteReviewsRepository;
    /**
     *setup the comment logic for all test cases here 
     */
    @BeforeAll
    public void setup()    {
        siteReviewsRepository = new CoreSiteReviewsRepository("/test/reviews/", "/test/reviews/arch/");        
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
            siteReviewsRepository.saveSiteReview(review);
        } catch (InvalidSiteReviewException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }
        //get the review here the sorting is done ascending to get the latest docs first
        List<SiteReview> siteReviews;
        try {
            siteReviews = siteReviewsRepository.getSiteReview(10, false);
        
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
            siteReviewsRepository.saveSiteReview(review);
        } catch (InvalidSiteReviewException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        //Find from the list the file to be archived
        List<SiteReview> siteReviews;
        try {
            siteReviews = siteReviewsRepository.getSiteReview(10, false);
             
            for(SiteReview siteReview : siteReviews) {
                if(siteReview.getTitle().equals(title)) {
                    // archive the file using the review Id
                    try {
                        siteReviewsRepository.archiveReview(siteReview.getReviewId());
                    } catch (InvalidSiteReviewException e) {                    
                        e.printStackTrace();
                    }
                    break;
                }
            }
        
            //Fetch the files again to check if the file exist        
            siteReviews = siteReviewsRepository.getSiteReview(10, false);
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
