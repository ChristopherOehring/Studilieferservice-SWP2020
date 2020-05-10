package com.studilieferservice.shoppinglistmanager.item;

import org.springframework.stereotype.Service;

@Service
public class ItemService {

    private final ItemRepository itemRepository;

    public ItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public void createItem(Item item) {
        itemRepository.save(item);
    }

//    public Item getItem(Long id) {
//
//        Item i = itemRepository.getOne(id);
//
//        return i;
//    }
}
