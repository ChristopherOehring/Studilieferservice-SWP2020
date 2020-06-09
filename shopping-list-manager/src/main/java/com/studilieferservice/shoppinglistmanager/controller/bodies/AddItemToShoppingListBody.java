package com.studilieferservice.shoppinglistmanager.controller.bodies;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;

public class AddItemToShoppingListBody {

    public Item item;
    public ShoppingList shoppingList;
    public int amount;

    public AddItemToShoppingListBody(@JsonProperty("item") Item item,
                                     @JsonProperty("shoppinglist") ShoppingList shoppingList,
                                     @JsonProperty("amount") int amount) {
        this.item = item;
        this.shoppingList = shoppingList;
        this.amount = amount;
    }
}
