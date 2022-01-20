/*
 * BU Term project for cs622
 
 interface to implement with functionality to send Notification to users using different channels
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.service;

import org.springframework.stereotype.Service;

import com.helpfinder.model.WorkInquiry;

@Service
public interface InquiryNotificationService {
	public void notifyUserAboutWork(WorkInquiry inquiry);

	public void notifyUserCommittedStatusChange(WorkInquiry inquiry);

	public void notifyWorkHireStatusChange(WorkInquiry inquiry);
}
