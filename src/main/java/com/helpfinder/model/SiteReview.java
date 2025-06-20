
/* 
 
 This class stores the information of a site review  
 * @author  Harish Janardhanan * 
 * @since   21-Jan-2022
 */
package com.helpfinder.model;
import java.io.Serializable;
import java.util.Date;

import com.helpfinder.common.DateFormatter;

//This class stores the information of a site review
public class SiteReview implements Serializable {
    /**
        * version of this class
        */
       private static final long serialVersionUID = 1L;
       //all properties
    private long userId;
    private String reviewId;
    private String title;
    private String comment;
    private Date submittedDate;
    private Date reviewedDate;
    
    //setters 
    public void setUserId(long userId) {
        this.userId = userId;        
    }
    
    public void setReviewId(String reviewId){
        this.reviewId = reviewId;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setComment(String comment){
        this.comment = comment;
    }
    
    public void setSubmittedDate(Date submittedDate){
        this.submittedDate = submittedDate;
    }
    
    public void setReviewedDate(Date reviewedDate){
        this.reviewedDate = reviewedDate;
    }
    
    //getters
    public long getUserId() {
        return userId;        
    }
    
    public String getTitle() {
        return title;        
    }
    
    public String getReviewId() {
        return reviewId;        
    }
    
    public  String getComment() {
        return comment;
    }
    
    public Date getSubmittedDate() {
        return submittedDate;
    }
    
    public  Date getReviewedDate() {
        return reviewedDate;
    }

    @Override
    public String toString() {
            /* string will be in this format
         * UserId
         * 23432 << some user id
         * Title
         * some title here
         * Comment
         * some comment here
         * ReviewSubmittedOn
         * review submitted date here
         * ReviewId 
         * the file path including file name
        */              
           return String.format("UserId\n%d\nTitle\n%s\nComment\n%s\nReviewSubmittedOn\n%s\nReviewID\n%s\nReviewedDate\n%s", this.getUserId(), this.getTitle(), 
                         this.getComment(), DateFormatter.convertToSystemDateString(this.getSubmittedDate()), getReviewId(),
                         getReviewedDate()  == null ? "" : DateFormatter.convertToSystemDateString(this.getReviewedDate()));
    }
    
}
