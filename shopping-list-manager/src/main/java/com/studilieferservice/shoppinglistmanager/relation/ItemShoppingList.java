package com.studilieferservice.shoppinglistmanager.relation;

import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity
//relation between ShoppingList and Item
//the relation is single-sided, only ShoppingList knows of its Item entities
public class ItemShoppingList implements Serializable {

    @EmbeddedId
    private ItemShoppingListPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("shopping_list_id")
    private ShoppingList shoppingList;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("item_id")
    private Item item;

    @NotNull
    private int amount;

    public ItemShoppingList() { }

    public ItemShoppingList(ShoppingList shoppingList, Item item, int amount) {
        this.shoppingList = shoppingList;
        this.item = item;
        this.amount = amount;
        this.id = new ItemShoppingListPK(shoppingList.getId(), item.getName());
    }

    public ItemShoppingListPK getId() {
        return id;
    }

    public void setId(ItemShoppingListPK id) {
        this.id = id;
    }

    public ShoppingList getShoppingList() {
        return shoppingList;
    }

    public void setShoppingList(ShoppingList shoppingList) {
        this.shoppingList = shoppingList;
    }

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format(
                "Item-ShoppingList [amount='%d', item.id='%s', item.name='%s', shoppingList.id='%s', shoppingList.group.id='%s', " +
                        "shoppingList.group.name='%s', shoppingList.user.id='%s', shoppingList.user.name='%s']",
                amount, id.getItemId(), item.getName(), id.getShoppingListId(), shoppingList.getGroup().getId(), shoppingList.getGroup().getName(),
                shoppingList.getUser().getId(), shoppingList.getUser().getName());
    }
}
