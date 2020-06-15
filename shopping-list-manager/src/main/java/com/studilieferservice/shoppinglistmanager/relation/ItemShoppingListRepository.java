package com.studilieferservice.shoppinglistmanager.relation;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemShoppingListRepository extends JpaRepository<ItemShoppingList, ItemShoppingListPK> {

}