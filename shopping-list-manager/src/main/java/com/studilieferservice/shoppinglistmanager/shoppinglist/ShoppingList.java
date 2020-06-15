package com.studilieferservice.shoppinglistmanager.shoppinglist;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingList;
import com.studilieferservice.shoppinglistmanager.user.User;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Entity
public class ShoppingList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JsonBackReference("user") //to avoid recursion in JSON
    private User user;

    @ManyToOne
    @JsonBackReference("group") //to avoid recursion in JSON
    private Group group;

    //actually a single-sided ManyToMany relation
    @OneToMany(mappedBy = "shoppingList")
    private List<ItemShoppingList> items = new ArrayList<>();

    //SHOULD NEVER BE USED: ShoppingLists cannot exist without a group or a user
    public ShoppingList() {}

    public ShoppingList(User user) {
        user.addShoppingList(this);
    }

    public ShoppingList(Group group) {
        group.addShoppingList(this);
    }

    public ShoppingList(User user, Group group) {
        user.addShoppingList(this);
        group.addShoppingList(this);
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    //do not use this method, use user.addShoppingList() or .removeShoppingList() instead
    public void setUser(User user) {
        this.user = user;
    }

    public Group getGroup() {
        return group;
    }

    //do not use this method, use group.addShoppingList() or .removeShoppingList() instead
    public void setGroup(Group group) {
        this.group = group;
    }

    public List<ItemShoppingList> getItems() {
        return items;
    }

    public void addItem(ItemShoppingList itemShoppingList) {
        items.add(itemShoppingList);
    }

    public void removeItem(Item item) {
        for (Iterator<ItemShoppingList> iterator = items.iterator(); iterator.hasNext();) {
            ItemShoppingList relation = iterator.next();

            if (relation.getShoppingList().equals(this) &&
                    relation.getItem().equals(item)) {
                iterator.remove();
                relation.setShoppingList(null);
                relation.setItem(null);
            }
        }
    }

    @Override
    public String toString() {
        return String.format(
                "ShoppingList [id='%d', group.id='%s', group.name='%s', user.id='%s', user.name='%s', itemsSize='%d', items=%s]",
                id, group.getId(), group.getName(), user.getId(), user.getName(), items.size(), Arrays.toString(items.toArray()));
    }
}
