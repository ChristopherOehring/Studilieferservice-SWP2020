package com.studilieferservice.chatmanager.group;

import com.studilieferservice.chatmanager.message.ChatMessage;
import com.studilieferservice.chatmanager.message.ChatMessage.MessageType;
import com.studilieferservice.chatmanager.message.ChatMessageService;
import com.studilieferservice.chatmanager.user.User;
import com.studilieferservice.chatmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final UserService userService;
    private final ChatMessageService chatMessageService;

    @Autowired
    public GroupService(GroupRepository groupRepository, UserService userService, ChatMessageService chatMessageService) {
        this.groupRepository = groupRepository;
        this.userService = userService;
        this.chatMessageService = chatMessageService;
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group getGroup(String id) {
        return groupRepository.findById(id).orElse(null);
    }

    public void deleteGroup(Group group) {
        Group g = groupRepository.findById(group.getId()).orElseThrow();

        for (Iterator<ChatMessage> iterator =
             groupRepository.findById(group.getId()).orElseThrow().getMessages().iterator(); iterator.hasNext();) {
            ChatMessage message = iterator.next();
            chatMessageService.deleteChatMessage(message);
            iterator.remove();
            message.setGroup(null);
        }

        groupRepository.delete(g);
    }

    public void addUserToGroup(User user, Group group) {
        User u = userService.getUser(user.getId());
        groupRepository.findById(group.getId()).orElseThrow().addUser(u);
    }

    //not to be used when iterating through the users list of a group
    public void removeUserFromGroup(User user, Group group) {
        User u = userService.getUser(user.getId());
        groupRepository.findById(group.getId()).orElseThrow().removeUser(u);
    }

    public List<ChatMessage> getAllChatMessagesOfGroup(Group group) {
        return groupRepository.findById(group.getId()).orElseThrow().getMessages();
    }

    @Transactional
    //Message uses username to identify user, not email; thus user might not be unique
    public ChatMessage addTextChatMessageToGroupAndUser(User user, Group group, String content) {
        ChatMessage m = new ChatMessage(userService.getUser(user.getId()).getName(), content, MessageType.MESSAGE);
        m = chatMessageService.createChatMessage(m);
        groupRepository.findById(group.getId()).orElseThrow().addMessage(m);
        return m;
    }

    public ChatMessage addJoinMessageToGroupAndUser(User user, Group group) {
        String content = user.getName() + " joined!";
        ChatMessage m = new ChatMessage(userService.getUser(user.getId()).getName(), content, MessageType.INFO);
        m = chatMessageService.createChatMessage(m);
        groupRepository.findById(group.getId()).orElseThrow().addMessage(m);
        return m;
    }

    public ChatMessage addLeaveMessageToGroupAndUser(User user, Group group) {
        String content = user.getName() + " left!";
        ChatMessage m = new ChatMessage(userService.getUser(user.getId()).getName(), content, MessageType.INFO);
        m = chatMessageService.createChatMessage(m);
        groupRepository.findById(group.getId()).orElseThrow().addMessage(m);
        return m;
    }

    public void addUpdateMessageToUser(User user, String oldName) {
        String content = oldName + " changed name to " + user.getName() + "!";

        for (Group group : user.getGroups()) {
            ChatMessage m = new ChatMessage(userService.getUser(user.getId()).getName(), content, MessageType.INFO);
            m = chatMessageService.createChatMessage(m);
            groupRepository.findById(group.getId()).orElseThrow().addMessage(m);
        }
    }

    @Transactional
    public ChatMessage addOrderMessageToGroupAndUser(User user, Group group, String date, String address) {
        String content = user.getName() + " has placed an order with the delivery date: " + date
                + " and the delivery address: " + address + "!";
        ChatMessage m = new ChatMessage(userService.getUser(user.getId()).getName(), content, MessageType.INFO);
        m = chatMessageService.createChatMessage(m);
        groupRepository.findById(group.getId()).orElseThrow().addMessage(m);
        return m;
    }
}
