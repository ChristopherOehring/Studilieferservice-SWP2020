package com.studilieferservice.shoppinglistmanager.controller;

import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/shoppingList")
public class ShoppingListRestController {

    private final ShoppingListService shoppingListService;

    @Autowired
    public ShoppingListRestController(ShoppingListService shoppingListService) {
        this.shoppingListService = shoppingListService;
    }

    @GetMapping("/")
    public String index() {
        return "It works! There is nothing more to see here";
    }

    @PostMapping("/create")
    public ShoppingList createShoppingList(@RequestBody ShoppingList shoppingList) {

        shoppingListService.createShoppingList(shoppingList);

        return shoppingList;
    }

    @DeleteMapping("/delete")
    public ShoppingList deleteShoppingList(@RequestBody ShoppingList shoppingList) {

        shoppingListService.deleteShoppingList(shoppingListService.getShoppingListByGroupId(shoppingList.getGroupId()));

        return shoppingList;
    }
}
