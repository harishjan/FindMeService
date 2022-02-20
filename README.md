# HelpFinder 

### Guides
This project is work in progress to run as RESTful api using springboot framework
NOTE:
This project is coded to run with java runtime version 8
THis project requires MAVEN version 2.6.2

These are the following packages in the project
* com.helpfinder: which contains the the spring boot application where the spring boot is begins the application.
* com.helpfinder.model: which contains all model classes used in the project
* com.helpfinder.repository: which contains all repos which connect to the respective data sources to add update delete data points.
* com.helpfinder.service: which contains all services through which different functionalities are exposed outside.
* com.helpfinder.controller: which contains controllers for the api endpoints.
* com.helpfinder.exception: which contains the user defined exceptions
* com.helpfinder.model.request: which contains the model used in a api request 

###Notes
1) User Repository is hooked up Sqlite db and it is working now, the sqlite db is is part of the project folder</br>
2) Swagger: Auth api functionality is implemented for user registration and signin, this can be verify through swagger</br>
3) Jwt Bearer token implementation is also complete.</br>
4) Login, search and work request flow is working now, location lat long is hard coded only in UI, the back is completely working</br>
5) To test login use this email password which is already registered in the system</br>  
    
    This is a worker user:
    Email: davidspellman@gmail.com
    Password: Test5@1234
    
    Email: This is a normal help finder user:
    test8897ch@gmail.com
    Password: Test5@1234
6) For search functionality there is a default lat long value which will pull some pre-registered users, this done only for demo, otherwise if we have proper user data the search should return result with lat long radius.

#####Details of testing Assignment 6 using JUnit####
Run below test in >> WorkforceLocatorServiceTest.java</br>
All data is mocked using mockito framework

>> test_findWorder_within_1_mile()
>> >> test_findWorder_within_1_mile()()()
>> test_Commit_Hire_cancel_WorkInquiry()
>> test_getInquiriesSent()
>> test_getInquiriesReceived()
 

#####Details about Assignment 5####
The code to demonstrate concurrency is based on a hypothetical  scenario.</br>
In this example a system is provided with a list of Jobs in text file where there will be following details:</br>
JobId: the id of the job</br>
Jobname: Name of the job performed</br>
Complexity: complexity defines the money paid for the job; workers make more money with higher complex work. (Complexity of work range from 1 - 100)</br>
E.g., of the input Job looks this</br>
1 job1 50 </br>
2 job2 60</br>
3 job3 50</br>
4 job4 70</br>
</br>
The Workers are also provided as an input text file where the following information is provided:</br>
Worker Id : Id of the worker</br>
Worker Name: Name of the worker</br>
Efficiency: Efficiency of the worker, a worker with higher efficiency can finish the work faster (Efficiency ranges from 1- 10).</br>
The amount of time to finish a work in millisecond for a work is defined as </br>
= (Complexity of the work / Efficiency of the work) * 1000</br>
E.g.: Worker input file looks like this:</br>
1 Worker1 5</br>
2 Worker2 9</br>
</br>
###To run unit test for Assignment 5 using junit##
Run below test in >> JobAllocationProcessTest.java</br>
>> test_AllTheJobsAreGettingProcessed()</br>

</br>
Output should look like this:</br>
+++++++++++++++++++++++++++++++++++++++++++Job Details++++++++++++++++++++++++++++++++++++++++++++++++++</br>
</br>
Job job2 completed by worker Sam and took 7000 ms to complete, and was awarded 0.600 $</br>
Job job1 completed by worker Peter and took 10000 ms to complete, and was awarded 0.500 $</br>
Job job3 completed by worker Sam and took 6000 ms to complete, and was awarded 0.500 $</br>
Job job5 completed by worker Sam and took 8000 ms to complete, and was awarded 0.700 $</br>
Job job4 completed by worker Peter and took 14000 ms to complete, and was awarded 0.700 $</br>
Job job7 completed by worker Peter and took 4000 ms to complete, and was awarded 0.200 $</br>
Job job6 completed by worker Sam and took 12000 ms to complete, and was awarded 1.000 $</br>
Job job9 completed by worker Sam and took 3000 ms to complete, and was awarded 0.300 $</br>
Job job8 completed by worker Peter and took 12000 ms to complete, and was awarded 0.600 $</br>
Job job10 completed by worker Sam and took 10000 ms to complete, and was awarded 0.800 $</br>
Job job12 completed by worker Sam and took 3000 ms to complete, and was awarded 0.300 $</br>
Job job13 completed by worker Sam and took 3000 ms to complete, and was awarded 0.300 $</br>
Job job11 completed by worker Peter and took 18000 ms to complete, and was awarded 0.900 $</br>
Job job14 completed by worker Sam and took 11000 ms to complete, and was awarded 0.900 $</br>
</br>
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++</br>
</br>
</br>
+++++++++++++++++++++++++++++++++++++++++Worker Details+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++</br>
</br>
Worker Sam earned 5.400 $ in total </br>
Worker Peter earned 2.900 $ in total </br>
</br>
++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++</br>
</br>

###To Run the tests for assignment 4 using JUnit :###

Run below test in >> SiteReviewsRepositoryTest.java</br>
1) test_Create_SiteReview_AsStream_and_check_StreamisSerialized_Properly()</br>
2) test_Create_SiteReviewStream_Archive_And_Check_if_Review_Exist()</br>




The output should look like below</br> and the persisted stream file should be in "test\reviewsstream "folder

Creating review with below details </br>
++++++++++++++++++++++++++++++++++++++++++++++++++</br>
UserId</br>
1</br>
Title</br>
big title2022/02/07 18:15:37.3811</br>
Comment</br>
a big bold comment</br>
ReviewSubmittedOn</br>
2022/02/07 00:00:00.0000</br>
ReviewID</br>
null</br>
ReviewedDate</br>

++++++++++++++++++++++++++++++++++++++++++++++++++</br>
Review serialized and stored successfully</br>
Review deserialized and pulled from file with below details</br>
++++++++++++++++++++++++++++++++++++++++++++++++++</br>
UserId</br>
1</br>
Title</br>
big title2022/02/07 18:15:37.3811</br>
Comment</br>
a big bold comment</br>
ReviewSubmittedOn</br>
2022/02/07 00:00:00.0000</br>
ReviewID</br>
C:\bu\CS622\ap2\helpfinder\test\reviewsstream\1_1e6a41d7-bb43-4e48-ab43-3f3b05a643b1.txt</br>
ReviewedDate</br>

++++++++++++++++++++++++++++++++++++++++++++++++++</br>
Creating review with below details </br>
++++++++++++++++++++++++++++++++++++++++++++++++++</br>
UserId</br>
1</br>
Title</br>
big title2022/02/07 18:15:37.4076</br>
Comment</br>
a big bold comment</br>
ReviewSubmittedOn</br>
2022/02/07 18:15:37.4070</br>
ReviewID</br>
null</br>
ReviewedDate</br>

++++++++++++++++++++++++++++++++++++++++++++++++++
Review serialized and stored successfully</br>
Review deserialized and pulled from file with below details</br>
++++++++++++++++++++++++++++++++++++++++++++++++++</br>
UserId</br>
1</br>
Title</br>
big title2022/02/07 18:15:37.4076</br>
Comment</br>
a big bold comment</br>
ReviewSubmittedOn</br>
2022/02/07 18:15:37.4070</br>
ReviewID</br>
C:\bu\CS622\ap2\helpfinder\test\reviewsstream\1_2fbc3729-987b-43d7-8b8c-f8a9fd15d5d0.txt</br>
ReviewedDate</br>

++++++++++++++++++++++++++++++++++++++++++++++++++</br>
Review serialized and archived successfully</br>
Review with title big title2022/02/07 18:15:37.4076 not found after archiving </br>



###To Run the tests for assignment 3 using JUnit and Mockito framework :###

Run below test in >> UserServiceTest.java
Test method test_getUserByEamil_checkif_genericType_returnsthe_CorrectPermission_based_onUsertype()


The output should look like below to verify the permission returned by different types of users</br>
+++++++++++++++++Test running for generics+++++++++++++++++++++++</br>
UserService returning a helpfinder user</br>
SEARCH_FOR_WORKERS permission found for Helpfinder user</br>
Remaining permission not found for helpfinder user</br>
++++++++++++++++++++++++++++++++++++++++++++</br>
UserService returning a worker user</br>
ALLOWED_TO_BE_HIRE permission found for worker user</br>
Remaining permission not found for worker user</br>
++++++++++++++++++++++++++++++++++++++++++++</br>
UserService returning a admin user</br>
SEARCH_FOR_WORKERS, ADD_ADMIN_USER, ARCH_SITE_FEEDBACK, DELETE_USER, REVIEW_SITE_FEEDBACK permission found for admin user</br>
Remaining permission not found for admin user</br>
++++++++++++++++++++++++++++++++++++++++++++</br>
UserService returning a moderator user</br>
ARCH_SITE_FEEDBACK, REVIEW_SITE_FEEDBACK, SEARCH_FOR_WORKERS, permission found for moderator user</br>
Remaining permission not found for moderator user</br>
+++++++++++++++++++End of generics tests+++++++++++++++++++++++++</br>
###To Run the tests for assignment 3 using JUnit and Mockito framework :###

Run below test in >> UserServiceTest.java
Test method test_getUserByEamil_checkif_genericType_returnsthe_CorrectPermission_based_onUsertype()


The output should look like below to verify the permission returned by different types of users</br>
+++++++++++++++++Test running for generics+++++++++++++++++++++++</br>
UserService returning a helpfinder user</br>
SEARCH_FOR_WORKERS permission found for Helpfinder user</br>
Remaining permission not found for helpfinder user</br>
++++++++++++++++++++++++++++++++++++++++++++</br>
UserService returning a worker user</br>
ALLOWED_TO_BE_HIRE permission found for worker user</br>
Remaining permission not found for worker user</br>
++++++++++++++++++++++++++++++++++++++++++++</br>
UserService returning a admin user</br>
SEARCH_FOR_WORKERS, ADD_ADMIN_USER, ARCH_SITE_FEEDBACK, DELETE_USER, REVIEW_SITE_FEEDBACK permission found for admin user</br>
Remaining permission not found for admin user</br>
++++++++++++++++++++++++++++++++++++++++++++</br>
UserService returning a moderator user</br>
ARCH_SITE_FEEDBACK, REVIEW_SITE_FEEDBACK, SEARCH_FOR_WORKERS, permission found for moderator user</br>
Remaining permission not found for moderator user</br>
+++++++++++++++++++End of generics tests+++++++++++++++++++++++++</br>

### Swagger UI ###
To access swagger 
* Run >> mvn spring-boot:run
* On a web browser access>> http://localhost:8099/api/swagger-ui/index.html
* auth-controller : /auth/* apis are implemented and can be tested via swagger

### Admin user cred in the db is###
email : adminuser@gmail.com
password: admin123

### Worker user cred in the db is###
email: testworkuser@gmail.com
password: testuser1

### helpfinder user cred in the db is###
email: helpfinder@gmail.com
password: testuser1

### moderator user cred in the db is###
email: moderatoruser@gmail.com,
password: mod123

### How to test the use cases for Assignment 2 ###

Run the test in >> UserServiceTest.java.
The output should look like below to verify the permission returned by different types of users
+++++++++++++++++Test running for generics+++++++++++++++++++++++
UserService returning a helpfinder user
SEARCH_FOR_WORKERS permission found for Helpfinder user
Remaining permission not found for helpfinder user
++++++++++++++++++++++++++++++++++++++++++++
UserService returning a worker user
ALLOWED_TO_BE_HIRE permission found for worker user
Remaining permission not found for workder user
++++++++++++++++++++++++++++++++++++++++++++
UserService returning a admin user
SEARCH_FOR_WORKERS, ADD_ADMIN_USER, ARCH_SITE_FEEDBACK, DELETE_USER, REVIEW_SITE_FEEDBACK permission found for admin user
Remaining permission not found for admin user
++++++++++++++++++++++++++++++++++++++++++++
UserService returning a moderator user
ARCH_SITE_FEEDBACK, REVIEW_SITE_FEEDBACK, SEARCH_FOR_WORKERS, permission found for moderator user
Remaining permission not found for moderator user
+++++++++++++++++++End of generics tests+++++++++++++++++++++++++

### How to test the use cases for Assignment 2 ###

1) For File I/O related use case, Run unit test SiteReviewsRespositoryTest.java.
This should create a folder in the root of the project file /test/reiviews/ and test/reviews/arch/
The folder /test/reiviews/ should have the files created for the feedback given by the user(this is based on test case >> test_Create_SiteRevie_check_The_review_saved()).
The folder test/reviews/arch/ will have the archived files once review is archived(this is based on test case >> test_Create_SiteReview_Archive_And_Check_if_Review_Exist()).

2) For User defined exception, run unit test UserServiceTest.java
The unit test case check if the UserExistException is throw while creating an already existing user



Following controllers are exposed[JWT token implementation is not complete]
1) site-review-controller - site review flow functionality is implemented through this controller.
2) user-controller - [DUMMY DATA]user basic operations are in this controller
3) auth-controller - [IMPLEMENTATION not complete]


### How to test The use cases for Assignment 1 ###


* open command prompt and Navigate to helpfinder\ project folder
* RUN > mvn clean install
* RUN > mvn compile exec:java -Dexec.mainClass="com.helpfinder.TestSkillFinder"

We should see a section similar to this in the output:
+++++++++ BEGINING USE CASE 1 TESTS +++++++++++++++

Created a HelpFinderUser, printing user details
Users should have lat long and userId assigned

The lat is 1.10 and log is 2.20
The User Id is 1642459563303

Created a WrokerUser, printing user details

The lat is 1.10 and log is 2.20
The User Id is 1642459563305

+++++++++ END USE CASE 1 TEST +++++++++++++++


+++++++++ BEGINING USE CASE 2 TESTS +++++++++++++++

Searching for users with in 1 mile..
1 matching user found, and now sending work request to the user

Email Sent requesting Work from waltDavid@test.com to user mattm@test.com >>
 Work start date: Mon Jan 17 end date: Wed Jan 19

Executing logic to commit a work

Email sent to waltDavid@test.com informing that user mattm@test.com committed the work

Executing logic to hire the work

Email sent to mattm@test.com informing that the waltDavid@test.com has hired the user for the work

+++++++++ END USE CASE 2 TESTS +++++++++++++++

###Directly run the test from eclipse

Test can be directly run from the eclipse if required by executing the test file >> com.helpfinder.TestSkillFinder.java
Unit test can be directly run from eclipse or by running mvn test