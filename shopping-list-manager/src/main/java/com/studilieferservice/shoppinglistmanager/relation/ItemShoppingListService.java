package com.studilieferservice.shoppinglistmanager.relation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemShoppingListService {

    private final ItemShoppingListRepository itemShoppingListRepository;

    @Autowired
    public ItemShoppingListService(ItemShoppingListRepository itemShoppingListRepository) {
        this.itemShoppingListRepository = itemShoppingListRepository;
    }

    public void createItemShoppingList(ItemShoppingList itemShoppingList) {
        itemShoppingListRepository.save(itemShoppingList);
    }

    public void deleteItemShoppingList(ItemShoppingList itemShoppingList) {
        itemShoppingListRepository.delete(itemShoppingList);
    }
}
