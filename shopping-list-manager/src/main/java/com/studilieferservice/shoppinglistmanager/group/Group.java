package com.studilieferservice.shoppinglistmanager.group;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "shopping_group")
//table name cannot be "group" as this word is an SQL statement and thus reserved
@JsonIgnoreProperties(ignoreUnknown = true)
public class Group {

    @Id
    private String id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "group")
    @JsonManagedReference("group") //to avoid recursion in JSON
    private List<ShoppingList> shoppingLists = new ArrayList<>();

    public Group() {}

    public Group(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ShoppingList> getShoppingLists() {
        return shoppingLists;
    }

    public void addShoppingList(ShoppingList shoppingList) {
        shoppingLists.add(shoppingList);
        shoppingList.setGroup(this);
    }

    public void removeShoppingList(ShoppingList shoppingList) {
        shoppingList.setGroup(null);
        shoppingLists.remove(shoppingList);
    }

    @Override
    public String toString() {
        StringBuilder users = new StringBuilder();

        for(ShoppingList sl : shoppingLists) {
            users.append("User [id=").append(sl.getUser().getId()).append(", name=").append(sl.getUser().getName()).append("], ");
        }
        if (users.length() > 0) {
            users.setLength(users.length() - 2);
        }

        return String.format(
                "Group [id='%s', name='%s', shoppingListsSize='%d', users=[%s]]",
                id, name, shoppingLists.size(), users);
    }
}