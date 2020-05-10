package com.studilieferservice.shoppinglistmanager.shoppinglist;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studilieferservice.shoppinglistmanager.item.Item;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Column(unique = true)
    private String groupId;

    @NotNull
    private String groupName;

    //ShoppingList 1-to-n Item(s)
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shoppingList", orphanRemoval = true, fetch = FetchType.EAGER)
    @JsonManagedReference //to avoid recursion in json
    private List<Item> items = new ArrayList<>();

    public ShoppingList() {}

    public ShoppingList(String groupId, String groupName) {
        this.groupId = groupId;
        this.groupName = groupName;
    }

    public Long getId() {
        return id;
    }

    public String getGroupId() {
        return groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public List<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
        item.setShoppingList(this);
    }

    public void removeItem(Item item) {
        item.setShoppingList(null);
        items.remove(item);
    }

    @Override
    public String toString() {
        return String.format(
                "ShoppingList [id=%d, groupId='%s', groupName='%s', #items='%d']",
                id, groupId, groupName, items.size());
    }
}
