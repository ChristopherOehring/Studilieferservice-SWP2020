package com.studilieferservice.shoppinglistmanager.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void createItem(Item item) {
        itemRepository.save(item);
    }

    public Item getItem(String name) {
        return itemRepository.findById(name).orElseThrow();
    }

    //public Item getItemByName(String name) {
    //    return itemRepository.findByName(name);
    //}

    public void deleteItem(Item item) {
        itemRepository.delete(item);
    }
}
