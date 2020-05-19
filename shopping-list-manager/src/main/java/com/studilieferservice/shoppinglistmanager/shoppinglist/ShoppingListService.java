package com.studilieferservice.shoppinglistmanager.shoppinglist;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository) {
        this.shoppingListRepository = shoppingListRepository;
    }

    public void createShoppingList(ShoppingList shoppingList) {

        shoppingListRepository.save(shoppingList);
    }

    public void deleteShoppingList(ShoppingList shoppingList) {

        shoppingListRepository.delete(shoppingList);
    }

/*    public ShoppingList getShoppingList(Long id) {

        ShoppingList s = shoppingListRepository.getOne(id);

        return s;
    }   */

    public ShoppingList getShoppingListByGroupId(String groupId) {

        ShoppingList s = shoppingListRepository.findByGroupId(groupId);

        return s;
    }
}
