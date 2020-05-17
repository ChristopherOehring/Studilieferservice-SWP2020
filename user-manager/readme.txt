To run the project install XAMPP on your computer and start the Mysql and Apache server
Go to localhost(without any port)-you should be able to see the XAMPP welcome page 
Click on phpmyadmin (right corner )and create a new data base with this name-  swp09_app_db
Go to intellij terminal and type -   ./mvnw spring-boot:run
Open localhost:8080 ,you should be able to see the start page

Theoretically, this module can now also be started via docker-compose, but it does not
work properly, because Docker and XAMPP do not work together and the started Docker container
has no access to the XAMPP database.
(When starting this module via docker-compose it stops after a short time which probably happens
due to the database errors occurring inside the container)
