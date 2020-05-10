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

    @GetMapping("/{groupId}")
    public String getShoppingListByGroupId(@PathVariable String groupId, Model model) {

        model.addAttribute("shoppingList", shoppingListService.getShoppingListByGroupId(groupId));
        model.addAttribute("item", new Item());

        return "list";
    }

    //HttpServletRequest is used in here for getting values from hidden fields (groupId)
    @PostMapping("/addItem")
    public String addItem(Item item, HttpServletRequest request) {

        ShoppingList shoppingList = shoppingListService.getShoppingListByGroupId(request.getParameter("groupId"));

        //makes sure users cannot enter empty items or whitespaces only
        if(!item.getName().matches(".*\\w.*")) {
            return "redirect:" + shoppingList.getGroupId();
        }

        item.setShoppingList(shoppingList);
        itemService.createItem(item);

        return "redirect:" + shoppingList.getGroupId();
    }
}
