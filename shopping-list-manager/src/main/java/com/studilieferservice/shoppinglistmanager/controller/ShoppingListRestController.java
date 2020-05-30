package com.studilieferservice.shoppinglistmanager.controller;

import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST Controller: accepts all REST requests of this module.
 * All requests are accepted at ".../shoppingList/".
 * @author Stefan Horst
 * @version 1.0
 */
@RestController
@RequestMapping("/shoppingList")
public class ShoppingListRestController {

    private final ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListRestController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
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
     * @param shoppingList in the body of the request: the {@link ShoppingList} that will be created and saved
     * @return the created {@link ShoppingList} as a JSON object
     */
    @PostMapping("/create")
    public ShoppingList createShoppingList(@RequestBody ShoppingList shoppingList) {

        shoppingListService.createShoppingList(shoppingList);

        return shoppingList;
    }

    //not yet in use
    /**
     * REST request for "/delete": deletes a previously {@link #createShoppingList(ShoppingList) created} shopping list.
     * @param shoppingList in the body of the request: the {@link ShoppingList} that will be deleted
     * @return the deleted {@link ShoppingList} as a JSON object
     */
    @DeleteMapping("/delete")
    public ShoppingList deleteShoppingList(@RequestBody ShoppingList shoppingList) {

        shoppingListService.deleteShoppingList(shoppingListService.getShoppingListByGroupId(shoppingList.getGroupId()));

        return shoppingList;
    }
}
