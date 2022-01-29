/*
 * BU Term project for cs622
 
  This class is the data access layer to get work force location related functionalities using sqlite as the db
  
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */

package com.helpfinder.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.helpfinder.model.User;
import com.helpfinder.model.WorkInquiry;
import com.helpfinder.model.WorkerSkill;

@Component
//This class is still not implemented these are dummy data
public class SqliteWorkForceLocatorRepository implements WorkForceLocatorRepository {

    // dummy data for testing
    static HashMap<Integer, WorkInquiry> dummaryWorkInquiries = new HashMap<Integer, WorkInquiry>();

    // store the user repo instance
    @Autowired
    private UserRepository userRepo;

    //@Autowired
    public SqliteWorkForceLocatorRepository(UserRepository userRepo) {
        this.userRepo = userRepo;

        // create some dummy data
        // inquiry 10 created for user1 sent to user 2
      /*  WorkInquiry inquiry1 = new WorkInquiry(10, new Date(System.currentTimeMillis()),
                new Date(new Date(System.currentTimeMillis()).getTime()  + 172800* 1000), // added 2 days
                this.userRepo.getUser(1), this.userRepo.getUser(2));

        // inquiry 11 created from user1 to user 3
        WorkInquiry inquiry2 = new WorkInquiry(11, new Date(System.currentTimeMillis()),
                new Date(new Date(System.currentTimeMillis()).getTime()  +  172800* 1000), // added 2 days
                this.userRepo.getUser(1), this.userRepo.getUser(3));

        dummaryWorkInquiries.put(10, inquiry1);
        dummaryWorkInquiries.put(11, inquiry2);*/

    }

    @Override
    public List<User> findWorkforceForSkills(Double[] latlong, List<WorkerSkill> skills, double mileRadius) {
        // TODO Auto-generated method stub returning dummy data
        List<User> match = new ArrayList<>();
        match.add((User)userRepo.getUser(2));
        return match;
    }

    @Override
    public WorkInquiry getInquiry(int inquiryId) {
        // TODO Auto-generated method stub
        return dummaryWorkInquiries.get(inquiryId);
    }

}
