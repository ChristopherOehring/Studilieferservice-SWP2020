package com.studilieferservice.shoppinglistmanager.shoppinglist;

import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.item.ItemService;
import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingList;
import com.studilieferservice.shoppinglistmanager.relation.ItemShoppingListService;
import com.studilieferservice.shoppinglistmanager.user.User;
import com.studilieferservice.shoppinglistmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.List;

@Service
public class ShoppingListService {

    private final ShoppingListRepository shoppingListRepository;
    private final GroupService groupService;
    private final UserService userService;
    private final ItemService itemService;
    private final ItemShoppingListService itemShoppingListService;

    @Autowired
    public ShoppingListService(ShoppingListRepository shoppingListRepository,
                               GroupService groupService, UserService userService, ItemService itemService,
                               ItemShoppingListService itemShoppingListService) {
        this.shoppingListRepository = shoppingListRepository;
        this.groupService = groupService;
        this.userService = userService;
        this.itemService = itemService;
        this.itemShoppingListService = itemShoppingListService;
    }

    public void createShoppingList(ShoppingList shoppingList) {
        shoppingListRepository.save(shoppingList);
    }

    public void deleteShoppingList(ShoppingList shoppingList) {
        groupService.getGroup(shoppingList.getGroup().getId()).removeShoppingList(shoppingList);
        userService.getUser(shoppingList.getUser().getId()).removeShoppingList(shoppingList);
        itemShoppingListService.deleteItemShoppingListWithShoppingList(shoppingList);
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
        return shoppingListRepository.findByUserAndGroup(user, group).orElse(null);
    }

    public int addItemToShoppingList(ShoppingList shoppingList, Item item, int amount) {
        ShoppingList sl = shoppingListRepository.findByUserAndGroup(shoppingList.getUser(), shoppingList.getGroup()).orElseThrow();
        Item i = itemService.getItem(item.getName());

        int amountOld = 0;

        if (!sl.getItems().contains(itemShoppingListService.getItemShoppingList(sl, i))) {
            ItemShoppingList relation = new ItemShoppingList(sl, i, amount);
            itemShoppingListService.createItemShoppingList(relation);

            sl.addItem(relation);
        } else {
            amountOld = itemShoppingListService.getItemShoppingList(sl, i).getAmount();
            itemShoppingListService.getItemShoppingList(sl, i).setAmount(amountOld + amount);
        }

        shoppingListRepository.save(sl);

        return amountOld + amount;
    }

    //reduces the int amount of the corresponding ItemShoppingList or deletes the ItemShoppingList if the amount reaches zero
    public int removeItemFromShoppingList(ShoppingList shoppingList, Item item, int amount) {
        ShoppingList sl = shoppingListRepository.findByUserAndGroup(shoppingList.getUser(), shoppingList.getGroup()).orElseThrow();
        Item i = itemService.getItem(item.getName());
        ItemShoppingList isl = itemShoppingListService.getItemShoppingList(sl, i);

        int amountTotal = isl.getAmount();
        //here, Math.max makes amountTotalNew unable to be smaller than zero
        int amountTotalNew = Math.max(0, amountTotal - amount);

        if (amountTotalNew <= 0) {
            deleteItemFromShoppingList(sl, i);
        } else {
            isl.setAmount(amountTotalNew);
            itemShoppingListService.createItemShoppingList(isl);
        }

        return amountTotalNew;
    }

    //removes and deletes the corresponding ItemShoppingList
    private void deleteItemFromShoppingList(ShoppingList shoppingList, Item item) {
        for (Iterator<ItemShoppingList> iterator = shoppingList.getItems().iterator(); iterator.hasNext();) {
            ItemShoppingList relation = iterator.next();

            if (relation.getShoppingList().equals(shoppingList) && relation.getItem().equals(item)) {
                itemShoppingListService.deleteItemShoppingList(relation);
                iterator.remove();
                relation.setShoppingList(null);
                relation.setItem(null);
            }
        }
    }

    //returns a custom JSON object containing the data of the group, its users and the shopping lists of the users
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
