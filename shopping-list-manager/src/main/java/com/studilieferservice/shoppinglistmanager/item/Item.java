package com.studilieferservice.shoppinglistmanager.item;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
public class Item {

    @Id
    @Column(name = "id")
    private String name;

    @NotNull
    private double price;

    //ShoppingList has a single-sided ManyToMany relation with this entity

    public Item() {}

    public Item(@JsonProperty("name") String name, @JsonProperty("price") double price) {
        this.name = name;
        this.price = price;
    }

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

    @Override
    public String toString() {
        return String.format(
                "Item [name='%s', price=%s€]",
                 name, price);
    }
}
