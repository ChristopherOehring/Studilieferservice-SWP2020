package com.studilieferservice.shoppinglistmanager.shoppinglist;

import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    //ShoppingList findByGroupId(String groupId);
    List<ShoppingList> findAllByGroup(Group group);

    ShoppingList findByUserAndGroup(User user, Group group);
}
