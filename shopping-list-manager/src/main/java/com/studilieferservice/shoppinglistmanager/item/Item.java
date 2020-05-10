package com.studilieferservice.shoppinglistmanager.item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    //maybe could be manyToMany later when items are unique as well (need to implement product-manager for that)
    @ManyToOne
    @JsonBackReference
    private ShoppingList shoppingList;

    public Item() {}

    public Item(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format(
                "Item [id=%d, name='%s', shoppingListId='%d', shoppingListGroupName='%s', shoppingListGroupId='%s']",
                id, name, shoppingList.getId(), shoppingList.getGroupName(), shoppingList.getGroupId());
    }
}
