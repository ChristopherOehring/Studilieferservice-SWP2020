package com.studilieferservice.shoppinglistmanager.controller;

import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * REST-Controller: nimmt alle REST-Anfragen dieses Moduls an.
 * Alle Anfragen werden unter ".../shoppingList/" angenommen.
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
     * REST-Anfrage für "/": zum Testen, ob das Modul funktioniert.
     * @return eine einfache Textnachricht
     */
    @GetMapping("/")
    public String index() {
        return "It works! There is nothing more to see here";
    }

    /**
     * REST-Anfrage für "/create": legt eine neue Einkaufsliste an, welche in der Datenbank gespeichert wird.
     * @param shoppingList im Body der Anfrage: die {@link ShoppingList}, die erstellt und gespeichert werden soll
     * @return die angelegte {@link ShoppingList} als Objekt
     */
    @PostMapping("/create")
    public ShoppingList createShoppingList(@RequestBody ShoppingList shoppingList) {

        shoppingListService.createShoppingList(shoppingList);

        return shoppingList;
    }

    //not yet in use
    /**
     * REST-Anfrage für "/delete": löscht eine zuvor {@link #createShoppingList(ShoppingList) angelegte} Einkaufsliste.
     * @param shoppingList im Body der Anfrage: die {@link ShoppingList}, die gelöscht werden soll
     * @return die gelöschte {@link ShoppingList} als Objekt
     */
    @DeleteMapping("/delete")
    public ShoppingList deleteShoppingList(@RequestBody ShoppingList shoppingList) {

        shoppingListService.deleteShoppingList(shoppingListService.getShoppingListByGroupId(shoppingList.getGroupId()));

        return shoppingList;
    }
}
