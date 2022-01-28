/*
 * BU Term project for cs622
 
  This class gives the functionality to find work force and the skills that are available in the market
  Use this class as a singleton instance
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.helpfinder.model.BasicUser;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;
import com.helpfinder.repository.WorkForceLocatorRepository;

@Service
public class WorkforceLocatorService {
    // instance of workforcelocator repository
    @Autowired
    private WorkForceLocatorRepository workforceLocatorRepo;
    // instance of User service
    @Autowired
    private UserService<BasicUser> userService;
    // instance through which notification are sent to user
    @Autowired
    private InquiryNotificationService notificationService;

    /**
     * Constructor     * 
     * @param workforceLocatorRepo the implementation of WorkForceLocatorRepository
     * @param userRepo             the implementation of UserRepository
     */    
    public WorkforceLocatorService(WorkForceLocatorRepository workforceLocatorRepo, UserService<BasicUser> userService,
            InquiryNotificationService notificationService) {
        this.workforceLocatorRepo = workforceLocatorRepo;
        this.userService = userService;
        this.notificationService = notificationService;
    }

    /**
     * Gets the workers who are available with the skill set with in the given mile
     * radius
     * 
     * @param User              the user requesting the work
     * @param List<WorkerSkill> the list of skills for which the search is performed
     * @param mileRadius        the mile radius within which the search is performed
     * @return List<User> list of matching users with skill set with in the mile
     *         radius
     */
    public List<User> findWorkforce(User RequestedBy, List<WorkerSkill> skills, Double mileRadius) {
        // hard coded
        // TODO get the users based on closest lat long radius
        List<User> matchingUsers = workforceLocatorRepo.findWorkforceForSkills(RequestedBy.getLatLong(), skills,
                mileRadius);
        return matchingUsers;
    }

    /**
     * create an inquiry in db, update the workers about the new work
     * 
     * @param List<WorkInquiry> the list of inquiries to be sent to different users
     */
    public void sendWorkInquiry(List<WorkInquiry> inquiries) {

        inquiries.forEach(inquiry -> {
            // TODO hard coded now
            // TODO: update worker user with the new inquiry in DB
            // update the user with the inquiry
            userService.getWorkInquirieReceived( inquiry.getWorkerUser().getUserId()).add(inquiry);
            // notify the user about the inquiry
            notificationService.notifyUserAboutWork(inquiry);
        });

    }

    /**
     * This method updates the user commit status for the for an inquiry *
     * 
     * @param inquiry inquiry to update
     * @param commit    the commit status
     */
    public void commitWork(WorkInquiry inquiry, boolean commit) {
        // TODO creating dummy data        
        
        userService.getWorkInquirieReceived( inquiry.getWorkerUser().getUserId()).add(inquiry);
        // notify the user about the update committed status
        notificationService.notifyUserCommittedStatusChange(inquiry);

    }

    /**
     * This method updates work inquiry as hired status
     * 
     * @param WorkInquiry inquiry to update
     * @param hired     the hired status
     */
    public void hireWork(WorkInquiry inquiry, boolean hired) {
        // TODO creating dummy data
        // workforceLocatorRepo.getInquiry(inquiryId)        

        userService.getWorkInquirieReceived( inquiry.getWorkerUser().getUserId()).add(inquiry);
        // notify the user about the update committed status
        notificationService.notifyWorkHireStatusChange(inquiry);

    }
}
