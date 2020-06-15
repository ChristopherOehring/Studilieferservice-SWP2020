package com.studilieferservice.shoppinglistmanager.shoppinglist;

import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.item.ItemService;
import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingList;
import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingListService;
import com.studilieferservice.shoppinglistmanager.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final GroupService groupService;
    private final ItemService itemService;
    private final ItemShoppingListService itemShoppingListService;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository,
                               GroupService groupService, ItemService itemService,
                               ItemShoppingListService itemShoppingListService) {
        this.shoppingListRepository = shoppingListRepository;
        this.groupService = groupService;
        this.itemService = itemService;
        this.itemShoppingListService = itemShoppingListService;
    }

    public void createShoppingList(ShoppingList shoppingList) {
        shoppingListRepository.save(shoppingList);
    }

    public void deleteShoppingList(ShoppingList shoppingList) {
        shoppingListRepository.delete(shoppingList);
    }

    public ShoppingList getShoppingList(Long id) {
        return shoppingListRepository.findById(id).orElseThrow();
    }

    public List<ShoppingList> getAllShoppingListsByGroupId(String groupId) {
        Group g = groupService.getGroup(groupId);
        return shoppingListRepository.findAllByGroup(g);
    }

    public ShoppingList getShoppingListByUserAndGroup(User user, Group group) {
        return shoppingListRepository.findByUserAndGroup(user, group);
    }

    public void addItemToShoppingList(ShoppingList shoppingList, Item item, int amount) {
        ShoppingList sl = shoppingListRepository.findByUserAndGroup(shoppingList.getUser(), shoppingList.getGroup());
        Item i = itemService.getItem(item.getName());

        ItemShoppingList relation = new ItemShoppingList(sl, i, amount);
        itemShoppingListService.createItemShoppingList(relation);

        sl.addItem(relation);
        shoppingListRepository.save(sl);
    }

    public String getCompleteShoppingListAsJSON(String groupId) {
        String group = String.format("\t\"id\": \"%s\",\n\t\"name\": \"%s\",\n\t",
                groupId, groupService.getGroup(groupId).getName());

        StringBuilder users = new StringBuilder("\"members\": [");
        List<ShoppingList> shoppingLists = groupService.getGroup(groupId).getShoppingLists();
        for (ShoppingList s : shoppingLists) {

            StringBuilder items = new StringBuilder();
            for (ItemShoppingList i : s.getItems()) {
                items.append("\n\t\t\t\t{\n\t\t\t\t\t\"name\": \"").append(i.getItem().getName()).
                        append("\",\n\t\t\t\t\t\"price\": \"").append(i.getItem().getPrice()).
                        append("\",\n\t\t\t\t\t\"amount\": \"").append(i.getAmount()).
                        append("\"\n\t\t\t\t}, ");
            }
            if (items.length() > 0)
                items.setLength(items.length() - 2);

            users.append("\n\t\t{\n\t\t\t\"id\": \"").append(s.getUser().getId()).
                    append("\",\n\t\t\t\"name\": \"").append(s.getUser().getName()).
                    append("\",\n\t\t\t\"items\": [").append(items).
                    append("\n\t\t\t]\n\t\t}, ");
        }
        users.setLength(users.length() - 2);
        users.append("\n\t]");

        return "{\n"+group+users+"\n}";
    }
}
