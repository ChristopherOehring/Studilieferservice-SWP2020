package com.studilieferservice.chatmanager.kafka.group;

import com.studilieferservice.chatmanager.group.Group;
import com.studilieferservice.chatmanager.group.GroupService;
import com.studilieferservice.chatmanager.user.User;
import com.studilieferservice.chatmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
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
            groupService.addJoinMessageToGroupAndUser(u, g);
        }
    }

    @Transactional
    public void deleteGroupFromKafka(String id) {
        Group g = groupService.getGroup(id);

        for (Iterator<User> iterator = g.getUsers().iterator(); iterator.hasNext();) {
            User u = iterator.next();

            iterator.remove();
            u.getGroups().remove(g);
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
            if (!g.getUsers().contains(u)) {
                groupService.addUserToGroup(u, g);
                groupService.addJoinMessageToGroupAndUser(u, g);
            }
        }

        //remove existing users
        for (Iterator<User> iterator = g.getUsers().iterator(); iterator.hasNext();) {
            User u = iterator.next();

            if (!users.contains(u)) {
                iterator.remove();
                u.getGroups().remove(g);

                groupService.addLeaveMessageToGroupAndUser(u, g);
            }
        }
    }
}
