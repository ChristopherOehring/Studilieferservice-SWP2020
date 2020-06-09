# User Manager
A basic user management microservice.

## Setup and Access
To run the project :

1.install Postgresql on your computer.

2.password für den User postgres ändern:
    sudo -u postgres psql -c "ALTER USER postgres PASSWORD 'password';"
    
3.create DB usermanager:

    CREATE DATABASE usermanager;
    
    3.1. connect to usermanager with 
         \c usermanager
    3.2. describe the DB with
         \d
    
4. install kafka on your computer.
    4.1 change your dir to kafka folder and then:
        4.1.1. run zookeeper server with:
                bin/zookeeper-server-start.sh config/zookeeper.properties
                
        4.1.2. open new terminal and run kafka server with:
                bin/kafka-server-start.sh config/server.properties
                
        4.1.3. open new terminal and create new topic with name "usersTopic" with (just once):
                bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic usersTopic
                
        4.1.4 open new terminal and run the consumer on this topic with:
                bin/kafka-console-consumer.sh --bootstrap-server localhost:9092 --topic usersTopic --from-beginning
        
          
5.user_manager App. starten.

6.Open http://localhost:8080, you should be able to see the start page.  
      




Using Docker (starting via docker-compose)
Theoretically, this module can also be started via docker-compose, but it does not 
work properly, because Docker and XAMPP do not work together: 
the started Docker container cannot access the XAMPP database. 
(When starting this module via docker-compose it stops after a short time which 
probably happens due to the database errors occurring inside the container.)

