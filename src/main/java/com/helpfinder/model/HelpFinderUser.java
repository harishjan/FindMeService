/*
 * BU Term project for cs622
 
  This class implements the user who are inquiring for help
 * @author  Harish Janardhanan * 
 * @since   16-jan-2022
 */
package com.helpfinder.model;

import java.util.ArrayList;
import java.util.List;

public class HelpFinderUser extends BasicUser {

	// empty constructor
	public HelpFinderUser() {
		this.userType = EUserEnum.ROLE_HELPFINDER_USER;
	}

	/**
	 * constructor
	 * 
	 * @param userId       user id
	 * @param address      the address of the user
	 * @param firstName    first name of the user
	 * @param lastname     last name of the user
	 * @param emailAddress email address of the user
	 */
	public HelpFinderUser(long userId, String address, String firstName, String lastname, String emailAddress) {
		this.userId = userId;
		this.userType = EUserEnum.ROLE_HELPFINDER_USER;
		super.setUserInformation(address, firstName, lastname, emailAddress);
	}

	/**
	 * gets the list of inquiries made where work user has committed
	 * 
	 * @return List<WorkInquiry> List of Work inquiries where worker has committed
	 */
	public List<WorkInquiry> getWorkCommitedInquiry() {
		// hard coded values for now
		List<WorkInquiry> workInquiries = getWorkInquiriesSent();
		// filter

		return workInquiries;

	}

	/**
	 * gets the list of inquiries made where the work has been hired
	 * 
	 * @return List<WorkInquiry> List of Work inquiries where the work has been
	 *         hired
	 */
	public List<WorkInquiry> getInquiryHired() {
		// hard coded values for now
		List<WorkInquiry> workInquiries = getWorkInquiriesSent();
		// filter
		return workInquiries;

	}

	/**
	 * gets the list of inquiries sent to user
	 * 
	 * @return List<WorkInquiry> List of Work inquiries
	 */
	public List<WorkInquiry> getWorkInquiriesSent() {

		// hard coded values for now
		List<WorkInquiry> workInquiries = new ArrayList<>();
		// if(userId == 1)

		return workInquiries;

	}

}
