/*
 
 
  This interface is the implementation for site review repository  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.repository;

import java.util.List;

import org.springframework.stereotype.Component;

import com.helpfinder.exception.InvalidSiteReviewException;
import com.helpfinder.model.SiteReview;

@Component
//This interface is the implementation for site review repository  
public interface SiteReviewsRepository {
    
    // save the site review
    public void saveSiteReview(SiteReview review) throws InvalidSiteReviewException;
    //get a list of review up to the number specified in the limit
    public List<SiteReview> getSiteReview(int limit, boolean sortDateDesc) throws InvalidSiteReviewException;
    //archive a review
    public void archiveReview(String id) throws InvalidSiteReviewException;

}
