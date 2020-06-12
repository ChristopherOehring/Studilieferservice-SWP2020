package com.studilieferservice.shoppinglistmanager.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item {

    //@Id
    //@GeneratedValue(strategy = GenerationType.AUTO)
    //private Long id;

    @Id
    private String name;

    @NotNull
    private double price;

    //actually a ManyToMany relation
    @OneToMany(mappedBy = "item")
    private List<ItemShoppingList> shoppingLists = new ArrayList<>();

    public Item() {}

    public Item(@JsonProperty("name") String name, @JsonProperty("price") double price) {
        this.name = name;
        this.price = price;
    }

    //public Long getId() {
    //    return id;
    //}

    public String getName() {
        return name;
    }

    private void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public List<ItemShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void addSL (ItemShoppingList itemshoppingList) {
        shoppingLists.add(itemshoppingList);
    }

//    public void addShoppingList(ShoppingList shoppingList) {
//        shoppingLists.add(shoppingList);
//        shoppingList.getItems().add(this);
//    }

//    public void removeShoppingList(ShoppingList shoppingList) {
//        shoppingList.getItems().remove(this);
//        shoppingLists.remove(shoppingList);
//    }

    @Override
    public String toString() {
        return String.format(
                "Item [name='%s', price='%s'€]", //%1$,.2f    id='%s',
                /*id,*/ name, price);
    }
}
