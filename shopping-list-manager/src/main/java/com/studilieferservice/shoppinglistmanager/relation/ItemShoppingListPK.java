package com.studilieferservice.shoppinglistmanager.relation;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
//holds composite entity identifier (ShoppingList and Item)
public class ItemShoppingListPK implements Serializable {

    @Column(name = "shopping_list_id")
    private Long shoppingListId;

    @Column(name = "item_id")
    private String itemId;

    public ItemShoppingListPK() { }

    public ItemShoppingListPK(Long shoppingListId, String itemId) {
        this.shoppingListId = shoppingListId;
        this.itemId = itemId;
    }

    public Long getShoppingListId() {
        return shoppingListId;
    }

    //private so only hibernate can access the method
    private void setShoppingListId(Long shoppingListId) {
        this.shoppingListId = shoppingListId;
    }

    public String getItemId() {
        return itemId;
    }

    //private so only hibernate can access the method
    private void setItemId(String itemId) {
        this.itemId = itemId;
    }

    //@Embeddable must override equals() and hashCode()

    @Override
    public boolean equals(Object object) {
        if (this == object)
            return true;

        if (object == null || getClass() != object.getClass())
            return false;

        ItemShoppingListPK objectCast = (ItemShoppingListPK) object;
        return Objects.equals(shoppingListId, objectCast.shoppingListId) &&
                Objects.equals(itemId, objectCast.itemId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shoppingListId, itemId);
    }
}
