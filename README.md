<h1>EMPLOYEE STATE MANAGEMENT REST API’S</h1>
<h1>Senior Challenge</h1>

<h2>Architecture</h2>
There are 1 controller and 2 repositories: EmployeeController, EmployeeRepository, StateLogRepository </br>
StateLog is used for logging stage changes of employees</br>
There is one-to-many relation between Employee and StateLog</br>
Project uses Spring Boot, H2 as database </br>
Spring State Machine 2.2.3 is used for the state management </br>
Project JDK is Amazon corretto-11 </br>

<h2>Business Rules</h2>
- Employee can be created with required fields : first name, last name, email, passport number </br>
- Employee's optional fields : address, age </br>
- Employees can't be added under the age 18 </br>
- Employee can't be deleted or updated except state changes </br>
- Employee's first name and last name length should be between 2 and 100 characters </br>
- Employee's email and passport number fields are unique to prevent creating same employee </br>
- When new employee has been added, its state becomes : ADDED </br>
- Employee states can be : ADDED, IN_CHECK, APPROVED, ACTIVE, SECURITY_CHECK_STARTED, SECURITY_CHECK_FINISHED, WORK_PERMIT_CHECK_STARTED, WORK_PERMIT_CHECK_FINISHED </br>
- Employee events can be triggered by changeState Post Method with the string event name in request body </br>
- Allowed events are : BEGIN_CHECK, ACTIVATE, FINISH_SECURITY_CHECK, FINISH_WORK_PERMIT_CHECK </br>
- After triggering BEGIN_CHECK event, employee state updates to IN_CHECK state </br>
- After triggering FINISH_SECURITY_CHECK and FINISH_WORK_PERMIT_CHECK, employee state automatically updates to APPROVED </br>
- After triggering ACTIVATE event, employee state updates to final ACTIVE event </br>
- Employee state can't be changed after ACTIVE state </br>
- All state changes are automatically logged to EMPLOYEE_STATE_LOG table in db </br>
- All employees can be queried by GetAllEmployees GET method with a paginated response </br>
- Employee state change logs can be queried by stateLogs GET method

<h2>Properties</h2>
- Application is secured. Default credentials for application and H2 db are: </br>
<B>username : Sysadmin</B> </br>
<B>password : password</B> </br>
Credentials can be changed from application.properties file </br>
- Basic Auth will be used to authenticate </br>
- Swagger documentation has been created, it can be accessed from : </br>
http://localhost:8080/swagger-ui/ </br>
- Postman Requests can be reached from : </br>
https://www.postman.com/collections/a8f3df17ec7c92997c7e </br>
- Employee entity model fields validation controls has been added </br>
- Transaction will not be commited to db if any error occurs during the operation </br>
- GetAllEmployees response is paginated, parameters like page size and order by can be changed on request </br>
- Custom exceptions and global error model has been added </br>

<h2>To run the application, execute:</h2>
mvn spring-boot:run
