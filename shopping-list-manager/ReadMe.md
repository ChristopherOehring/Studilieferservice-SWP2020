# Shopping List Manager
A basic shopping list microservice which allows you to create a list and add items to it.

## Setup and access
Locally the application runs on port `8070`.
Via docker-compose the application runs on port `9070`.

To see if everything works go to http://localhost:8070/shoppingList/.
There you should be able to see a simple message.

## Using Docker (starting via docker-compose)
This module can be started via docker-compose and works fine.
However, it is possible that other modules cannot access this module when it is
started using Docker.

## Create a new shopping list
You can create new shopping lists with POST.
Using Postman etc., run a POST request at `http://localhost:8070/shoppingList/create` with the body:
```
{
     "groupId": "gruppeTestId",
     "groupName": "Testgruppe"
}
```
 This will create a new shopping list that belongs to the defined group.
 You can change the values of these parameters to whatever you want.
 
 ## Access existing shopping lists
 After creating a shopping list, you can access it by going to http://localhost:8070/shoppingList/gruppeTestId in your browser.
 Basically you can access shopping lists by putting their `groupId` in the URL behind `http://localhost:8070/shoppingList/`.
 
 There you can add items by typing their names in the text field and pressing "Hinzufügen".
 they should now appear below in a list and are automatically saved.
 
 ### Accessing the database directly
 If run locally, you can also access the H2 Database by going to http://localhost:8070/console/ in your browser.
 The login data is: 
 ```
JDBC URL: jdbc:h2:mem:shopping-list-manager 
User Name: sa
Password: [empty]
```