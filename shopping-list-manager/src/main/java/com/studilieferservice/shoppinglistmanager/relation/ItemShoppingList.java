package com.studilieferservice.shoppinglistmanager.relation;

import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
//relation between ShoppingList and Item
public class ItemShoppingList {

    @EmbeddedId
    private ItemShoppingListPK id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("shoppingListId")
    private ShoppingList shoppingList;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("itemId")
    private Item item;

    @NotNull
    private int amount;

    public ItemShoppingList() { }

    public ItemShoppingList(ShoppingList shoppingList, Item item, int amount) {
        this.shoppingList = shoppingList;
        this.item = item;
        this.amount = amount;
        this.id = new ItemShoppingListPK(shoppingList.getId(), item.getId());
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
