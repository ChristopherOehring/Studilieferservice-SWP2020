package com.studilieferservice.shoppinglistmanager.shoppinglist;

import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingList;
import com.studilieferservice.shoppinglistmanager.user.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final GroupService groupService;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository, GroupService groupService) {
        this.shoppingListRepository = shoppingListRepository;
        this.groupService = groupService;
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

    public String getCompleteShoppingListAsJSON(String groupId) {
        String group = String.format("\"id\": \"%s\",\n\"name\": \"%s\",\n",
                groupId, groupService.getGroup(groupId).getId());

        StringBuilder users = new StringBuilder("\"members\": [\n");
        List<ShoppingList> shoppingLists = groupService.getGroup(groupId).getShoppingLists();
        for (ShoppingList s : shoppingLists) {

            StringBuilder items = new StringBuilder();
            for (ItemShoppingList i : s.getItems()) {
                items.append("\t\t{\n\t\t\"id\": \"").append(i.getItem().getId()).
                        append("\",\n\t\t\"name\": \"").append(i.getItem().getName()).
                        append("\",\n\t\t\"price\": \"").append(i.getItem().getPrice()).
                        append("\",\n\t\t\"amount\": \"").append(i.getAmount()).
                        append("\"\n\t\t}, ");
            }
            if (items.length() > 0)
                items.setLength(items.length() - 2);

            users.append("\t{\n\t\"id\": \"").append(s.getUser().getId()).
                    append("\",\n\t\"name\": \"").append(s.getUser().getName()).
                    append("\",\n\t\"items\": [\n").append(items).
                    append("\n\t]\n\t}, ");
        }
        users.setLength(users.length() - 2);
        users.append("\n]");

        return "{\n"+group+users+"\n}";
    }
}
