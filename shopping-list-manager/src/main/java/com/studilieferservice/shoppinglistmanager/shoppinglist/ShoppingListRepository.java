package com.studilieferservice.shoppinglistmanager.shoppinglist;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    ShoppingList findByGroupId(String groupId);
}
