package com.studilieferservice.shoppinglistmanager.shoppinglist;

import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ShoppingListRepository extends JpaRepository<ShoppingList, Long> {

    List<ShoppingList> findAllByGroup(Group group);

    Optional<ShoppingList> findByUserAndGroup(User user, Group group);
}
