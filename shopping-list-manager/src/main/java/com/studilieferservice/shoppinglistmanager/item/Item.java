package com.studilieferservice.shoppinglistmanager.item;

import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private double price;

    //actually a ManyToMany relation
    @OneToMany(mappedBy = "item")
    private List<ItemShoppingList> shoppingLists = new ArrayList<>();

    public Item() {}

    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
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
                "Item [id='%s', name='%s', price='%s'€]", //%1$,.2f
                id, name, price);
    }
}
