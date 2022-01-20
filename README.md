# HelpFinder 

### Guides
This project is work in progress to run as RESTful api using springboot framework
NOTE:
This project is coded to run with java runtime version 15
THis project requires MAVEN version 2.6.2

These are the following packages in the project
* com.helpfinder: which contains the the spring boot application where the spring boot is begins the application.
* com.helpfinder.model: which contains all model classes used in the project
* com.helpfinder.repository: which contains all repos which connect to the respective data sources to add update delete data points.
* com.helpfinder.service: which contains all services through wich different functionalities are exposed outside.
* com.helpfinder.controller: which contains controllers for the api endpoints.

###Notes
This project does not have any integration with any repository data sources, currently there are dummy data points which helps to test assignment 1 use cases.

### How to test The use cases 


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