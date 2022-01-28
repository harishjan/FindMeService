/*
 * BU Term project for cs622
 
 Functionalities to submit and review the site feedbacks are exposed through this class
 * @author  Harish Janardhanan * 
 * @since   23-jan-2022
 */

package com.helpfinder.service;

import java.security.InvalidParameterException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpfinder.exception.InvalidSiteReviewException;
import com.helpfinder.model.SiteReview;
import com.helpfinder.repository.SiteReviewsRepository;

//Functionalities to submit and review the site feedbacks are exposed through this class
@Service
public class SiteReviewService {
    @Autowired
    SiteReviewsRepository siteReviewsRepository;
    
    /**
     * Saves the review in a file
     * @param review the review object which should be saved
     */
    public void submitSiteReview(SiteReview review) throws InvalidSiteReviewException {
        if(review == null || review.getUserId() < 1 || review.getComment() == null
                || review.getTitle() == null)
            throw new InvalidSiteReviewException("Site review Data is invalid");
        
        siteReviewsRepository.saveSiteReview(review);
    }
    
    /**
     * gets a list of reviews up to the limit sorted by date descending of the file created
     * @param limit the number of reviews to return
     * @param sortDateDesc boolean value indicating if the reviews fetched are to be sorted descending based on reviewed date  
       
     */
    public List<SiteReview> getSiteReview(int limit, boolean sortDateDesc) throws InvalidParameterException, InvalidSiteReviewException{
        // can only get max of 20 files
        if(limit < 1)
            throw new InvalidParameterException("limit should be greater than 0");
        // process only to max limit
        int maxLimit = 20;// need to add this in properties
        if(limit > maxLimit)
            limit = maxLimit;
        return siteReviewsRepository.getSiteReview(limit, sortDateDesc);
    }
    
    
    /**
     * Moves the review to archived folder
     * @param id the source file name with the full path which should be archived
     * @throws IOException 
     */
    public void archiveReview(String reviewId) throws InvalidSiteReviewException {        
        siteReviewsRepository.archiveReview(reviewId);        
    }
}
