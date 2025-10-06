# Bajaj Finserv Health Challenge - Question 1 Solution

**Author:** Aman Biswakarma  
**Registration:** 112215015  
**Email:** 112215015@cse.iiitp.ac.in

### SQL Query Development
For Question 1 (odd registration number), the SQL solution:

```sql
SELECT 
    p.AMOUNT AS SALARY,
    CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME,
    TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE,
    d.DEPARTMENT_NAME
FROM PAYMENTS p
JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID
JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID
WHERE DAY(p.PAYMENT_TIME) != 1
ORDER BY p.AMOUNT DESC
LIMIT 1;
```

**Query Logic:**
- Joins PAYMENTS, EMPLOYEE, and DEPARTMENT tables
- Filters out transactions made on the 1st day of any month
- Orders by salary amount in descending order
- Returns the highest salary with employee details

## Solution Flow

The application follows this automated workflow:

1. **Webhook Generation**: Sends POST request to generate webhook with credentials
2. **Authentication**: Receives JWT access token and webhook URL
3. **Question Detection**: Determines Question 1 based on odd last 2 digits of regNo (15)
4. **Query Loading**: Loads the SQL solution from `question1.sql`
5. **Database Storage**: Saves the solution to H2 database for persistence
6. **Solution Submission**: Submits final query using JWT authentication


## Local Testing

Thoroughly tested the solution using **Postman** to verify:

### Test 1: Webhook Generation
- **Method**: POST
- **URL**: `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
- **Body**: 
```json
{
  "name": "Aman Biswakarma",
  "regNo": "112215015", 
  "email": "112215015@cse.iiitp.ac.in"
}
```
<img width="1759" height="598" alt="image" src="https://github.com/user-attachments/assets/5bfaacf3-477a-47e4-99e1-a7940a3a2703" />

- **Result**: Successfully received JWT token and webhook URL

### Test 2: Solution Submission
- **Method**: POST
- **URL**: `https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA`
- **Headers**: `Authorization: Bearer <JWT_TOKEN>`
- **Body**: 
```json
{
  "finalQuery": "SELECT p.AMOUNT AS SALARY, CONCAT(e.FIRST_NAME, ' ', e.LAST_NAME) AS NAME, TIMESTAMPDIFF(YEAR, e.DOB, CURDATE()) AS AGE, d.DEPARTMENT_NAME FROM PAYMENTS p JOIN EMPLOYEE e ON p.EMP_ID = e.EMP_ID JOIN DEPARTMENT d ON e.DEPARTMENT = d.DEPARTMENT_ID WHERE DAY(p.PAYMENT_TIME) != 1 ORDER BY p.AMOUNT DESC LIMIT 1;"
}
```
<img width="1759" height="566" alt="image" src="https://github.com/user-attachments/assets/9f8c1783-a280-4a41-ac4b-7a5ceea4e723" />

- **Result**: Successfully submitted SQL solution

## Setup & Execution

### Prerequisites
- Java 17+
- Maven 3.6+

### Configuration
Update `src/main/resources/application.yml`:
```yaml
app:
  generate-webhook-url: "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA"
  test-webhook-url: "https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA"
  regNo: "112215015"
  name: "Aman Biswakarma"
  email: "112215015@cse.iiitp.ac.in"
```

### Build & Run
```bash
# Build the project
mvn clean package

# Run the application
java -jar target/bajaj-finserv-health-solution-0.0.1-SNAPSHOT.jar
```

### Expected Output
```
Starting Bajaj Finserv Health Challenge Flow...
Step 1: Generating webhook...
Webhook generated successfully. Webhook URL: https://bfhldevapigw.healthrx.co.in/hiring/testWebhook/JAVA
Determined question ID: 1 for regNo: 112215015
Loaded SQL query for question 1: SELECT p.AMOUNT AS SALARY...
Solution saved to database with ID: 1
Step 5: Submitting final query to webhook...
Challenge completed successfully! Final query submitted.
```


# REFERENCE: Challenge details (Using LLM)
## Problem Statement

Find the highest salary that was credited to an employee, with the condition that the transaction was **NOT** made on the 1st day of any month.

**Expected Output:**
- `SALARY`: The highest salary amount
- `NAME`: Employee's full name (First Name + Last Name)  
- `AGE`: Employee's age
- `DEPARTMENT_NAME`: Department name

## Solution Approach

### Challenge Analysis
The challenge requires:
- Use WebClient with Spring Boot
- No controller/endpoint should trigger the flow
- JWT authentication required for the second API call
- Automatic execution on application startup

### Architecture Design
Clean, modular architecture with:
- **Service Layer**: `SolutionService` - Core business logic
- **Client Layer**: `WebhookClient` - HTTP communication
- **Data Layer**: JPA entities and repositories
- **Utility Layer**: SQL query provider
- **Configuration**: Spring Boot auto-configuration

## Technical Implementation

### Technologies Used
- **Java 17** - Modern Java features
- **Spring Boot 3.2.5** - Application framework
- **Spring WebFlux** - Reactive WebClient for HTTP calls
- **Spring Data JPA** - Database operations
- **H2 Database** - In-memory database
- **Lombok** - Reduces boilerplate code
- **Maven** - Dependency management

### Key Features
- **WebClient Integration**: Reactive HTTP client for API calls
- **Automatic Execution**: Runs on startup via CommandLineRunner
- **JWT Authentication**: Proper Bearer token implementation
- **Database Persistence**: Stores solutions in H2 database
- **Error Handling**: Comprehensive logging and exception handling
- **Modular Design**: Clean separation of concerns

## Error Handling

The solution includes comprehensive error handling:
- **Network errors**: WebClient exception handling with detailed logging
- **JWT expiration**: Graceful handling of expired tokens
- **Database errors**: JPA exception handling
- **File loading errors**: SQL file loading with fallback handling
