package com.studilieferservice.shoppinglistmanager.item;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemRepository extends JpaRepository<Item, String> {

    //Item findByName(String name);
}