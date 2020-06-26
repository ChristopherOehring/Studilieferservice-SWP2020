package com.studilieferservice.chatmanager.kafka.group;

import com.studilieferservice.chatmanager.group.Group;
import com.studilieferservice.chatmanager.group.GroupService;
import com.studilieferservice.chatmanager.user.User;
import com.studilieferservice.chatmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GroupKafkaService {

    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public GroupKafkaService(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    @Transactional
    public void createGroupFromKafka(String id, String groupName, List<String> usersString) {
        Group g = groupService.createGroup(new Group(id, groupName));

        List<User> users = userService.convertUsersFromStringToObject(usersString);
        //users do not need to be saved because users must already exist to be members of a group
        for (User u : users) {
            groupService.addUserToGroup(u, g);
        }
    }

    public void deleteGroupFromKafka(String id, List<String> usersString) {
        Group g = groupService.getGroup(id);

        List<User> users = userService.convertUsersFromStringToObject(usersString);

        for (User u : users) {
            groupService.removeUserFromGroup(u, g);
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
            if (!g.getUsers().contains(u))
                g.addUser(u);
        }

        //remove existing users
        for (User u : g.getUsers()) {
            if (!users.contains(u))
                g.removeUser(u);
        }
    }
}
