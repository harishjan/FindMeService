/*
 
 
 Functionalities to send email notification to users for inquiries are implemented in this class
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.service;

import org.springframework.stereotype.Service;

import com.helpfinder.model.WorkInquiry;

@Service
//implementation is not complete
public class InquiryEmailNotificationService implements InquiryNotificationService {

    /**
     * This method sends an email notification to the work to inform about the work inquiry
     * @param inquiry the inquiry for which the notification is being sent
     */
    @Override
    public void notifyUserAboutWork(WorkInquiry inquiry) {
        // TODO Auto-generated method stub
        System.out.println(String.format("Email Sent requesting Work from %s to user %s >> \n Work start date: %s end date: %s", inquiry.getHelpFinderUser().getEmailAddress(),
                inquiry.getWorkerUser().getEmailAddress(), inquiry.getWorkStartDate().toString().substring(0, 10), inquiry.getWorkEndDate().toString().substring(0, 10)));

    }
    
    /**
     * This method sends an email notification to the help finder user inform that a work has committed to the work
     * @param inquiry the inquiry for which the notification is being sent
     */

    @Override
    public void notifyUserCommittedStatusChange(WorkInquiry inquiry) {
        // TODO Auto-generated method stub
        System.out.println(String.format("Email sent to %s informing that user %s committed the work", inquiry.getHelpFinderUser().getEmailAddress(),
                inquiry.getWorkerUser().getEmailAddress()));
    }

    /**
     * This method sends an email notification to the worker that the user is hired for the work
     * @param inquiry the inquiry for which the notification is being sent
     */
    @Override
    public void notifyWorkHireStatusChange(WorkInquiry inquiry) {
        // TODO Auto-generated method stub
        System.out.println(String.format("Email sent to %s informing that the %s has hired the user for the work", 
                inquiry.getWorkerUser().getEmailAddress(), inquiry.getHelpFinderUser().getEmailAddress()));
    }

}
