
/*
 * BU Term project for cs622
 
Site review related functionalities are expose through this controller
 * @author  Harish Janardhanan * 
 * @since   23-Jan-2022
 */
package com.helpfinder.controller;

import java.security.InvalidParameterException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.helpfinder.exception.InvalidSiteReviewException;
import com.helpfinder.model.SiteReview;
import com.helpfinder.service.SiteReviewService;

@RestController
@RequestMapping(value = "/sitereview")
//Site review related functionalities are expose through this controller
public class SiteReviewController {
    
    @Autowired
    SiteReviewService siteReviewService;
    
    /***
     * Archive a site review
     * @param siteReviewId the reviewId which should be archived
     * @return status of archive
     */
    @RequestMapping(value = "/archiveSiteReview", method = RequestMethod.POST)
    public ResponseEntity<?> archiveSiteReview(@RequestParam("siteReviewId") String siteReviewId) {
        
        try {
            siteReviewService.archiveReview(siteReviewId);            
            return  ResponseEntity.status(HttpStatus.OK).body("Site review archived successfully");
        } catch (InvalidSiteReviewException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch(Exception ex)        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    
    /***
     * Submit a site review
     * @param siteReview the site review instance
     * @return status of the submission
     */
    @RequestMapping(value = "/submitsitereview", method = RequestMethod.POST, consumes = "application/json", produces = "application/json"
            )
    public ResponseEntity<?> submitSiteReview(@RequestBody SiteReview siteReview) {
        
        try {
            siteReviewService.submitSiteReview(siteReview);            
            return  ResponseEntity.status(HttpStatus.OK).body("Site review submitted successfully");
        } catch (InvalidSiteReviewException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }catch(Exception ex)        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
    }
    
    /**
     * get a list of site review up to the limit and the sorting order of date the reviews were created
     * @param limit the number of site reviews to fetch
     * @param sortDateDesc boolean value indicating if the reviews needs to be sorted in desc order of date created
     * @return List<SiteReview> a list of site reviews
     */     
    @RequestMapping(value = "/getSiteReviews", method = RequestMethod.GET)
    public ResponseEntity<?> getSiteReviews( @RequestParam int limit, @RequestParam boolean sortDateDesc) {
        
        try {
            System.out.print("here1");
            // get the site reviews
            List<SiteReview> siteReviews = siteReviewService.getSiteReview(limit, sortDateDesc);
            System.out.print("here2");
            
            // return appropriate response
            return siteReviews == null || siteReviews.size() == 0 ? ResponseEntity.status(HttpStatus.NOT_FOUND).body("No site reviews") : ResponseEntity.ok(siteReviews);
        }catch(InvalidParameterException | InvalidSiteReviewException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
        catch(Exception ex)        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
        }
        
    }

}
