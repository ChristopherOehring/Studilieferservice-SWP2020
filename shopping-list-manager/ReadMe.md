# Shopping List Manager
A basic shopping list microservice which allows you to create a list and add items to it.

## Setup and access
Locally the application runs on port `8070`.
Via docker-compose the application runs on port `9070` (recommended).

To see if everything works go to http://localhost:9070/shoppingList/.
There you should be able to see a simple message.

## Using Docker (starting via docker-compose)
This module should be started via docker-compose and is by default configured to run with Docker.

## Create a new shopping list (in the console / with Postman)
Before being able to access shopping lists directly (in the browser) you need to create a few database entries 
(preferably with Postman or cURL).

**1. User**

First, you have to create a user (users can also be "automatically" created (consumed) via Kafka). 

Using Postman etc., run a POST request at `http://localhost:9070/shoppingList/user/create` with the body:
```
{
     "email": "usermail",
     "username": "testuser"
}
```

**2. Group**

Next, you have to create a group (groups can also be "automatically" created (consumed) via Kafka). 

Using Postman etc., run a POST request at `http://localhost:9070/shoppingList/group/create` with the body:
```
{
     "id": "groupid1",
     "name": "testgroup"
}
```

**3. Product (Item)**

Next, you have to create a product (products can also be "automatically" created (consumed) via Kafka). 

Using Postman etc., run a POST request at `http://localhost:9070/shoppingList/product/create` with the body:
```
{
     "name": "testproduct",
     "price": 1.99
}
```

**4. Shopping List**

Next, you have to create a shopping list for the created user belonging to the created group. 

Using Postman etc., run a POST request at `http://localhost:9070/shoppingList/create` with the body:
```
{
    "user": {
        "email": "usermail"
    },
    "group": {
        "id": "groupid1"
    }
}
```

**5. Add Item to Shopping List**

Lastly, you have to add the item to the shopping list (at least once, so it is displayed in the browser).

Using Postman etc., run a PUT request at `http://localhost:9070/shoppingList/addItem` with the body:
```
{
    "item": {
        "name": "testproduct"
    },
    "shoppinglist": {
        "user": {
            "email": "usermail"
        },
        "group": {
            "id": "groupid1"
        }
    },
    "amount": 10
}
```

 These steps will create a new shopping list that belongs to the group and the user.
 By creating the shopping list the user gets added to the group.
 The item needs to be added once to the shopping list (amount does not matter), because otherwise 
 it will not be displayed in the browser. 
 
 Items can be removed by using the same body as `addItem`, but using the request `removeItem`.
 If the given amount is so high that the total existing amount reaches zero, the item will be deleted from the shopping list.
 
 You can change the values of these parameters to whatever you want.
 
 ## Access existing shopping lists (in the browser)
 After creating a shopping list, you can access it by going to http://localhost:9070/shoppingList/groupid1/usermail in your browser.
 Basically you can access shopping lists by putting their `groupId` followed by a `/` and their `userId` in the URL 
 behind `http://localhost:9070/shoppingList/`.
 
 There you can add items (increase their amount) by pressing the "+" or "-" symbol next to them.
 
 ### Accessing the database directly
 **PostgreSQL**
 
 When run using Docker, you can access the database by going to http://localhost:9002 in your browser. 
 The login data is:
 ```
 Datenbank System	PostgreSQL
 Server                  db
 Benutzer	        db_user
 Passwort	        db_pwd
 Datenbank	        postgres
 ```
 
 **H2 (legacy)**
 
 If run locally, you can also access the H2 Database by going to http://localhost:8070/console/ in your browser 
 (you need to switch to H2 in the pom.xml and the application.properties file in order for this to work).
 The login data is: 
 ```
JDBC URL: jdbc:h2:mem:shopping-list-manager 
User Name: sa
Password: [empty]
```