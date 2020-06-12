package com.studilieferservice.shoppinglistmanager.controller;

import com.studilieferservice.shoppinglistmanager.controller.bodies.AddItemToShoppingListBody;
import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.item.ItemService;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import com.studilieferservice.shoppinglistmanager.user.User;
import com.studilieferservice.shoppinglistmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller: accepts all REST requests of this module.
 * All requests are accepted at ".../shoppingList/".
 * @author Stefan Horst
 * @version 1.1
 */
@RestController
@RequestMapping("/shoppingList")
public class ShoppingListRestController {

    private final ShoppingListService shoppingListService;
    private final UserService userService;
    private final GroupService groupService;
    private final ItemService itemService;

    @Autowired
    public ShoppingListRestController(ShoppingListService shoppingListService, UserService userService,
                                      GroupService groupService, ItemService itemService) {
        this.shoppingListService = shoppingListService;
        this.userService = userService;
        this.groupService = groupService;
        this.itemService = itemService;
    }

    /**
     * REST request for "/": for testing purposes, to see if the module works.
     * @return a simple text message
     */
    @GetMapping("/")
    public String index() {
        return "It works! There is nothing more to see here";
    }

    /**
     * REST request for "/create": creates a new shopping list which is saved in the database.
     *
     * @param shoppingList in the body of the request: the {@link ShoppingList} that will be created and saved
     *        JSON body example:
     *        {
     *            "user": {
     *                "email": "useremail"
     *            },
     *            "group": {
     *                "id": "groupid"
     *            }
     *        }
     * @return HTTP status code and short infotext
     */
    @PostMapping("/create")
    public ResponseEntity<?> createShoppingList(@RequestBody ShoppingList shoppingList) {
        shoppingListService.createShoppingList(shoppingList);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created ShoppingList: "+shoppingList.toString());
    }

    //not yet in use
    /**
     * REST request for "/delete": deletes a previously {@link #createShoppingList(ShoppingList) created} shopping list.
     *
     * @deprecated needs to be updated
     * @param shoppingList in the body of the request: the {@link ShoppingList} that will be deleted
     *        JSON body example:
     *        {
     *            "user": {
     *                "email": "useremail"
     *            },
     *            "group": {
     *                "id": "groupid"
     *            }
     *        }
     * @return HTTP status code and short infotext
     */
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteShoppingList(@RequestBody ShoppingList shoppingList) {

        shoppingListService.deleteShoppingList(shoppingListService.getShoppingListByUserAndGroup(
                shoppingList.getUser(), shoppingList.getGroup()));

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Deleted ShoppingList: "+shoppingList.toString());
    }

    /**
     * REST request for "/user/create": creates a new user who is saved in the database.
     *
     * @param user in the body of the request: the {@link User} that will be created and saved
     *        JSON body example:
     *        {
     *            "email": "useremail",
     *            "userName": "username"
     *        }
     * @return HTTP status code and short infotext
     */
    @PostMapping("/user/create")
    public ResponseEntity<?> createUser(@RequestBody User user) {
        userService.createUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created User: "+user.toString());
    }

    /**
     * REST request for "/group/create": creates a new group which is saved in the database.
     *
     * @param group in the body of the request: the {@link Group} that will be created and saved
     *        JSON body example:
     *        {
     *            "id": "groupid",
     *            "name": "groupname"
     *        }
     * @return HTTP status code and short infotext
     */
    @PostMapping("/group/create")
    public ResponseEntity<?> createGroup(@RequestBody Group group) {
        groupService.createGroup(group);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Group: "+group.toString());
    }

    /**
     * REST request for "/product/create": creates a new product(/item) which is saved in the database.
     *
     * @param item in the body of the request: the {@link Item} that will be created and saved
     *        JSON body example:
     *        {
     *            "name": "productname",
     *            "price": 1.00
     *        }
     * @return HTTP status code and short infotext
     */
    @PostMapping("/product/create")
    public ResponseEntity<?> createItem(@RequestBody Item item) {
        itemService.createItem(item);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created Item: "+item.toString());
    }

    /**
     * REST request for "/addProduct": adds an {@link Item} to a {@link ShoppingList} and saves the amount of items
     * in the relation between the two objects.
     *
     * @param body in the body of the request: a special class for representing the body: {@link AddItemToShoppingListBody}
     *             which contains objects for an {@link Item}, a {@link ShoppingList} and an Integer for the amount of items
     *        JSON body example:
     *        {
     *            "item": {
     *                "name": "productname"
     *            },
     *            "shoppinglist": {
     *               "user": {
     *                   "email": "useremail"
     *               },
     *               "group": {
     *                   "id": "groupid"
     *               }
     *            },
     *            "amount": 1
     *        }
     * @return HTTP status code and short infotext
     */
    @PostMapping("/addProduct")
    public ResponseEntity<?> addItemToShoppingList(@RequestBody AddItemToShoppingListBody body) {
        System.out.println(body.item.toString()+" "+body.shoppingList.toString()+" "+body.amount);
        shoppingListService.addItemToShoppingList(body.shoppingList, body.item, body.amount);

        //Item i = itemService.getItem(body.item.getName());
        //ShoppingList sl =  shoppingListService.getShoppingListByUserAndGroup(body.shoppingList.getUser(), body.shoppingList.getGroup());
        //sl.addItem(i, body.amount);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body("Added Item to ShoppingList: "+body.item.toString());
    }
}
