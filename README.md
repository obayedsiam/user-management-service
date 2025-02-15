# User Management System

## Overview
This project is a **User Management System** that allows you to manage users, roles, permissions, and handle authentication with JWT. The system provides endpoints for creating, updating, deleting, and viewing users, along with role and permission management.

## Features
- **JWT-based authentication**: Secure login and token generation.
- **Role-based access control**: Users are assigned roles with different permission sets.
- **Permission management**: Manage permissions like CREATE, READ, UPDATE, DELETE for users.
- **Database**: MySQL-based persistence for storing user information.

## Technologies Used
- **Spring Boot** for backend framework
- **MySQL** for database
- **Spring Security** for authentication and authorization
- **Swagger** for API documentation

## API Documentation
Swagger UI is available at: [Swagger UI](http://localhost:9494/swagger-ui/index.html#/)

## Database Configuration

### MySQL Database
The database `UserDB` is used for storing user and role information. The following table structures are used:

1. **USER** table:
   - `USER_ID`: Unique identifier for each user (primary key).
   - `USER_NAME`: Name of the user.
   - `PASSWORD`: Encrypted password.
   - `EMAIL`: Email address (unique).
   - `ROLE_ID`: Foreign key to the `ROLE` table.

2. **ROLE** table:
   - `ROLE_ID`: Unique identifier for each role (primary key).
   - `ROLE_NAME`: The name of the role (e.g., ADMIN, USER, SUPER_ADMIN).

3. **USER_PERMISSIONS** table:
   - `USER_ID`: Foreign key to the `USER` table.
   - `PERMISSION`: Enum for permissions like CREATE, READ, UPDATE, DELETE.

## Setup Instructions

1. **Clone the repository**:
   ```bash
   git clone https://github.com/obayedsiam/user-management-service.git

## Configure database:

Ensure that MySQL is installed and running.
Create a database named UserDB in MySQL.
Update the database connection URL in application.properties.

## Run the application:

Run the application with the command:
bash
Copy
Edit
mvn spring-boot:run
Access Swagger:

Once the application is running, visit http://localhost:9494/swagger-ui/index.html#/ to view the API documentation.

Configuration
Application Configuration
The following configurations are provided in the application.properties:

properties
Copy
Edit
## Server Port
server.port=9494

## DataSource Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/UserDB

spring.datasource.username=root

spring.datasource.password=

spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

spring.main.allow-bean-definition-overriding=true

## JPA Properties
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQLDialect

## Hibernate Properties for Schema Auto-update
spring.jpa.hibernate.ddl-auto=update

## JWT Values
security.jwt.secret-key=Yp2s5w8zB4e7v9yGqfThVrXxZ3u5w8zB4e7v9yGqfThVrXxZ3u5w8zB4e7v9yGqfThVrXxZ3u5w8z

security.jwt.expiration-time=86400000  # 1 hour in milliseconds

## Let me know if you need further clarification on any endpoint or additional details!
