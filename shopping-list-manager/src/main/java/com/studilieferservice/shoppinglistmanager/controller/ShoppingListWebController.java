package com.studilieferservice.shoppinglistmanager.controller;

import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.item.ItemService;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * Web Controller: accepts all web requests of this module and returns corresponding HTML pages.
 * All requests are accepted at ".../shoppingList/".
 * @author Stefan Horst
 * @version 1.0
 */
@Controller
@RequestMapping("/shoppingList")
public class ShoppingListWebController {

    private final ShoppingListService shoppingListService;
    private final ItemService itemService;

    @Autowired
    public ShoppingListWebController(ShoppingListService shoppingListService, ItemService itemService) {
        this.shoppingListService = shoppingListService;
        this.itemService = itemService;
    }

    /**
     * Web request (GET) for "/[Group-ID]": retrieves the page of the shopping list for the respective group.
     * @param groupId The group-ID which is passed as a parameter through the URL
     * @param model Spring specific: the {@code model} of the requested {@link ShoppingList}, the data of which is dynamically inserted into the list.html
     * @return the HTML page "list.html" which displays the shopping list
     */
    @GetMapping("/{groupId}")
    public String getShoppingListByGroupId(@PathVariable String groupId, Model model) {

        model.addAttribute("shoppingList", shoppingListService.getShoppingListByGroupId(groupId));
        model.addAttribute("item", new Item());

        return "list";
    }

    /**
     * Web request (POST) for "/addItem": adds an item to the shopping list that is currently being displayed.
     * @param item the {@link Item} that will be added to the {@link ShoppingList}
     * @param request Spring specific: for accessing the group-ID which is stored the body of the HTML page "list.html"
     * @return a redirection to {@link #getShoppingListByGroupId(String, Model) getShoppingListByGroupId} with the current group-ID
     */
    //HttpServletRequest is used in here for getting values from hidden fields (groupId)
    @PostMapping("/addItem")
    public String addItem(Item item, HttpServletRequest request) {

        ShoppingList shoppingList = shoppingListService.getShoppingListByGroupId(request.getParameter("groupId"));

        //makes sure users cannot enter empty items or whitespaces only (needs to be improved)
        if(!item.getName().matches(".*\\w.*")) {
            return "redirect:" + shoppingList.getGroupId();
        }

        item.setShoppingList(shoppingList);
        itemService.createItem(item);

        return "redirect:" + shoppingList.getGroupId();
    }
}
