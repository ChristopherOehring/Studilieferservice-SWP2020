package com.studilieferservice.chatmanager.controller;

import com.studilieferservice.chatmanager.controller.bodies.OrderBody;
import com.studilieferservice.chatmanager.group.Group;
import com.studilieferservice.chatmanager.group.GroupService;
import com.studilieferservice.chatmanager.message.ChatMessage;
import com.studilieferservice.chatmanager.user.User;
import com.studilieferservice.chatmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class ChatRestController {

    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public ChatRestController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    @PostMapping("/addOrder")
    public ResponseEntity<?> addOrderMessage(@RequestBody OrderBody orderBody) {
        Group g = groupService.getGroup(orderBody.groupId);
        User u = userService.getUser(orderBody.userId);

        ChatMessage m = groupService.addOrderMessageToGroupAndUser(u, g, orderBody.date, orderBody.address);

        return ResponseEntity.status(HttpStatus.CREATED).body("Added order message to group: \"" + g.getName()
                + "\" with id: " + g.getId() + " and message: " + m.toString());
    }
}
