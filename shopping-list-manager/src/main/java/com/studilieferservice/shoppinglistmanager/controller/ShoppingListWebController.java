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
 * Web-Controller: nimmt alle Web-Anfragen dieses Moduls an und gibt entsprechende HTML-Seiten zurück.
 * Alle Anfragen werden unter ".../shoppingList/" angenommen.
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
     * Web-Anfrage (GET) für "/[Gruppen-ID]": ruft die Webseite für die Einkaufsliste zur jeweiligen Gruppe bzw. Gruppen-ID auf.
     * @param groupId Die im URL-Pfad angegebene Gruppen-ID
     * @param model Spring-spezifisch: das {@code model} der angefragten Einkaufsliste, dessen Daten in die list.html eingefügt werden
     * @return die HTML-Seite "list.html", die eine Einkaufsliste anzeigt
     */
    @GetMapping("/{groupId}")
    public String getShoppingListByGroupId(@PathVariable String groupId, Model model) {

        model.addAttribute("shoppingList", shoppingListService.getShoppingListByGroupId(groupId));
        model.addAttribute("item", new Item());

        return "list";
    }

    /**
     * Web-Anfrage (POST) für "/addItem": fügt der aktuell angezeigten Einkaufsliste einen Artikel hinzu.
     * @param item der eingegebene Artikel
     * @param request Spring-spezifisch: daraus kann die in der HTML-Seite "list.html" hinterlegte Gruppen-ID entnommen werden
     * @return eine Weiterleitung auf {@link #getShoppingListByGroupId(String, Model) getShoppingListByGroupId} mit der aktuellen Gruppen-ID
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
