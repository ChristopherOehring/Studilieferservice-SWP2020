package com.studilieferservice.shoppinglistmanager.kafka.group;

import com.studilieferservice.shoppinglistmanager.group.Group;
import com.studilieferservice.shoppinglistmanager.group.GroupService;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingList;
import com.studilieferservice.shoppinglistmanager.shoppinglist.ShoppingListService;
import com.studilieferservice.shoppinglistmanager.user.User;
import com.studilieferservice.shoppinglistmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public void createGroupFromKafka(String id, String groupName, List<String> usersString) {
        Group g = groupService.createGroup(new Group(id, groupName));

        List<User> users = userService.convertUsersFromStringToObject(usersString);
        //users do not need to be saved because users must already exist to be members of a group
        for (User u : users) {
            ShoppingList shoppingList = new ShoppingList(u, g);
            shoppingListService.createShoppingList(shoppingList);
        }
    }
}
