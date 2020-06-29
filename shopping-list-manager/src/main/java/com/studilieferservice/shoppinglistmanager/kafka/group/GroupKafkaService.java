package com.studilieferservice.shoppinglistmanager.kafka.group;

import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import com.studilieferservice.shoppinglistmanager.user.User;
import com.studilieferservice.shoppinglistmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupKafkaService {

    private final ShoppingListService shoppingListService;
    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public GroupKafkaService(ShoppingListService shoppingListService, UserService userService, GroupService groupService) {
        this.shoppingListService = shoppingListService;
        this.userService = userService;
        this.groupService = groupService;
    }

    @Transactional
    public void createGroupFromKafka(String id, String groupName, List<String> usersString) {
        Group g = groupService.createGroup(new Group(id, groupName));

        List<User> users = userService.convertUsersFromStringToObject(usersString);
        //users do not need to be saved because users must already exist to be members of a group
        for (User u : users) {
            ShoppingList shoppingList = new ShoppingList(u, g);
            shoppingListService.createShoppingList(shoppingList);
        }
    }

    @Transactional
    public void deleteGroupFromKafka(String id, List<String> usersString) {
        Group g = groupService.getGroup(id);

        List<User> users = userService.convertUsersFromStringToObject(usersString);

        for (User u : users) {
            ShoppingList shoppingList = shoppingListService.getShoppingListByUserAndGroup(u, g);
            shoppingListService.deleteShoppingList(shoppingList);
        }

        groupService.deleteGroup(g);
    }

    public boolean groupAlreadyExists(String id) {
        Group g = groupService.getGroup(id);

        return g != null;
    }

    @Transactional
    //only updates members of the group (if new ones are added or existing ones removed)
    public void updateGroupFromKafka(String id, List<String> usersString) {
        Group g = groupService.getGroup(id);

        List<User> users = userService.convertUsersFromStringToObject(usersString);

        //add new users
        for (User u : users) {
            if (shoppingListService.getShoppingListByUserAndGroup(u, g) == null) {
                ShoppingList shoppingList = new ShoppingList(u, g);
                shoppingListService.createShoppingList(shoppingList);
            }
        }

        //remove existing users
        for (ShoppingList sl : shoppingListService.getAllShoppingListsByGroupId(g.getId())) {
            User u = sl.getUser();

            if (!users.contains(u))
                shoppingListService.deleteShoppingList(sl);
        }
    }
}
