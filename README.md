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
1) This project does not have any integration with any repository data sources, Sqlite data repository is created but the hook-up with the repository classes are pending. currently there are dummy data points which helps to test assignment 1 and 2 user cases related to users.
2) This Project is integrated with repositories where I/O read/write operation are working specific for Assignment 2
3) Swagger integration is working with-out any security features which is in progress.

### How to test the use cases for Assignment 2 ###

1) For File I/O related use case, Run unit test SiteReviewsRespositoryTest.java.
This should create a folder in the root of the project file /test/reiviews/ and test/reviews/arch/
The folder /test/reiviews/ should have the files created for the feedback given by the user(this is based on test case >> test_Create_SiteRevie_check_The_review_saved()).
The folder test/reviews/arch/ will have the archived files once review is archived(this is based on test case >> test_Create_SiteReview_Archive_And_Check_if_Review_Exist()).

2) For User defined exception, run unit test UserServiceTest.java
The unit test case check if the UserExistException is throw while creating an already existing user

### Swagger UI ###
To access swagger 
1) Run >> mvn spring-boot:run
2) On a web browser access>> http://localhost:8099/api/swagger-ui/index.html


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