package com.studilieferservice.shoppinglistmanager.relation;

import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
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
    public ItemShoppingList getItemShoppingList(ShoppingList shoppingList, Item item) {
        return itemShoppingListRepository.findByShoppingListAndItem(shoppingList, item);
    }

    public void deleteItemShoppingList(ItemShoppingList itemShoppingList) {
        itemShoppingListRepository.delete(itemShoppingList);
    }

    public void deleteItemShoppingListWithShoppingList(ShoppingList shoppingList) {
        for (ItemShoppingList i : itemShoppingListRepository.findAll()) {
            if (i.getShoppingList().equals(shoppingList))
                deleteItemShoppingList(i);
        }
    }
}
