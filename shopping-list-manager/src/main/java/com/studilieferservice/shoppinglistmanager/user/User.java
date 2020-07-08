package com.studilieferservice.shoppinglistmanager.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Entity
//table name cannot be "user" as this word is reserved in Postgres
@Table(name = "shopping_user")
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    //email address
    private String id;

    @NotNull
    private String name;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference("user") //to avoid recursion in JSON
    private List<ShoppingList> shoppingLists = new ArrayList<>();

    public User() {}

    public User(@JsonProperty("email") String id,
                @JsonProperty("userName") String name) {
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
        shoppingList.setUser(this);
    }

    public void removeShoppingList(ShoppingList shoppingList) {
        shoppingList.setUser(null);
        shoppingLists.remove(shoppingList);
    }

    @Override
    public String toString() {
        StringBuilder groups = new StringBuilder();

        for(ShoppingList sl : shoppingLists) {
            groups.append("Group [id=").append(sl.getGroup().getId()).append(", name=").append(sl.getGroup().getName()).append("], ");
        }
        if (groups.length() > 0) {
            groups.setLength(groups.length() - 2);
        }

        return String.format(
                "User [id='%s', name='%s', shoppingListsSize='%d', groups=[%s]]",
                id, name, shoppingLists.size(), groups);
    }
}