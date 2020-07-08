package com.studilieferservice.shoppinglistmanager.controller;

import com.studilieferservice.shoppinglistmanager.controller.bodies.GroupBody;
import com.studilieferservice.shoppinglistmanager.controller.bodies.ItemAndShoppingListBody;
import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.item.ItemService;
import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingList;
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
     * REST request (GET) for "/": for testing purposes, to see if the module works.
     * @return a simple text message
     */
    @GetMapping("/")
    public String index() {
        return "It works! There is nothing more to see here";
    }

    /**
     * REST request (POST) for "/create": creates a new shopping list which is saved in the database.
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

    /**
     * REST request (DELETE) for "/delete": deletes a previously {@link #createShoppingList(ShoppingList) created} shopping list.
     *
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

        return ResponseEntity.status(HttpStatus.OK).body("Deleted ShoppingList: "+shoppingList.toString());
    }

    /**
     * REST request (POST) for "/user/create": creates a new user who is saved in the database.
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
     * REST request (POST) for "/group/create": creates a new group which is saved in the database.
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
     * REST request (POST) for "/product/create": creates a new product(/item) which is saved in the database.
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
     * REST request (PUT) for "/addProductToList": adds an {@link Item} to a {@link ShoppingList} and saves the amount of items
     * in the {@link ItemShoppingList relation} between the two objects.
     *
     * @param body in the body of the request: a special class for representing the body: {@link ItemAndShoppingListBody}
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
    @PutMapping("/addProductToList")
    public ResponseEntity<?> addItemToShoppingList(@RequestBody ItemAndShoppingListBody body) {
        int amountNew = shoppingListService.addItemToShoppingList(body.shoppingList, body.item, body.amount);
        return ResponseEntity.status(HttpStatus.CREATED).body("Added Item to ShoppingList: amount="+body.amount
                +", newTotalAmount="+amountNew+", "+body.item.toString()+", "+body.shoppingList.toString());
    }

    /**
     * REST request (PUT) for "/removeProduct": decreases the amount variable in the {@link ItemShoppingList} of the given
     * {@link Item} and {@link ShoppingList} or removes the {@link Item} from the {@link ShoppingList} and deletes the
     * corresponding {@link ItemShoppingList} if the amount is decreased to zero.
     *
     * @param body in the body of the request: a special class for representing the body: {@link ItemAndShoppingListBody}
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
    @PutMapping("/removeProduct")
    public ResponseEntity<?> removeItemFromShoppingList(@RequestBody ItemAndShoppingListBody body) {
        int amountNew = shoppingListService.removeItemFromShoppingList(body.shoppingList, body.item, body.amount);
        return ResponseEntity.status(HttpStatus.CREATED).body("Removed Item from ShoppingList: amount="+body.amount
                +", newTotalAmount="+amountNew+", "+body.item.toString()+", "+body.shoppingList.toString());
    }

    @PutMapping("/removeAllProductsForGroup")
    public ResponseEntity<?> removeAllItemsFromShoppingListForAllUsersOfGroup(@RequestBody GroupBody groupBody) {
        Group g = groupService.getGroup(groupBody.groupId);

        shoppingListService.deleteAllItemsFromShoppingListsForGroup(g);

        return ResponseEntity.status(HttpStatus.CREATED).body("Removed all Items from all Users for Group: \""
                + g.getName() + "\" with id: " + g.getId());
    }

    /**
     * REST request (GET) for "/getComplete/[Group-ID]": gets the complete shopping list of the respective group.
     * @param groupId The group-ID which is passed as a parameter through the URL
     * @return a custom JSON Object containing the data of the {@link Group}, its {@link User} and the
     * {@link ShoppingList ShoppingLists} of the users
     */
    @GetMapping("/getComplete/{groupId}")
    public ResponseEntity<?> getCompleteShoppingListAsJSON(@PathVariable String groupId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                shoppingListService.getCompleteShoppingListAsJSON(groupId));
    }
}
