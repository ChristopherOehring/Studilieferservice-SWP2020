package com.studilieferservice.shoppinglistmanager.relation;

import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemShoppingListRepository extends JpaRepository<ItemShoppingList, ItemShoppingListPK> {

    ItemShoppingList findByShoppingListAndItem(ShoppingList shoppingList, Item item);
}