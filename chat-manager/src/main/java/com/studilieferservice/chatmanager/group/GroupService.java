package com.studilieferservice.chatmanager.group;

import com.studilieferservice.chatmanager.message.ChatMessage;
import com.studilieferservice.chatmanager.user.User;
import com.studilieferservice.chatmanager.user.UserRepository;
import com.studilieferservice.chatmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserService userService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group getGroup(String id) {
        return groupRepository.findById(id).orElse(null);
    }

    public void deleteGroup(Group group) {
        groupRepository.delete(group);
    }

    public void addUserToGroup(User user, Group group) {
        User u = userService.getUser(user.getId());
        groupRepository.findById(group.getId()).orElseThrow().addUser(u);
    }

    public void removeUserFromGroup(User user, Group group) {
        User u = userService.getUser(user.getId());
        groupRepository.findById(group.getId()).orElseThrow().removeUser(u);
    }

    public List<ChatMessage> getAllChatMessagesOfGroup(Group group) {
        return groupRepository.findById(group.getId()).orElseThrow().getMessages();
    }

    //Message uses username to identify user, not email; thus user might not be unique
    public ChatMessage addChatMessageToGroupAndUser(User user, Group group, String content) {
        ChatMessage m = new ChatMessage(userService.getUser(user.getId()).getName(), content);
        groupRepository.findById(group.getId()).orElseThrow().addMessage(m);
        return m;
    }
}
