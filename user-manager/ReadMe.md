# User Manager
A basic user management microservice.

## Setup and Access
To run the project install XAMPP on your computer and start the Mysql and Apache server.
Go to http://localhost (without any port) - you should be able to see the XAMPP welcome page. 
Click on phpmyadmin (right corner) and create a new database with the name `swp09_app_db`.
Go to the IntelliJ terminal and type `./mvnw spring-boot:run` or run the main application.
Open http://localhost:8080, you should be able to see the start page.

## Using Docker (starting via docker-compose)
Theoretically, this module can also be started via docker-compose, but it does not 
work properly, because Docker and XAMPP do not work together: 
the started Docker container cannot access the XAMPP database. 
(When starting this module via docker-compose it stops after a short time which 
probably happens due to the database errors occurring inside the container.)

