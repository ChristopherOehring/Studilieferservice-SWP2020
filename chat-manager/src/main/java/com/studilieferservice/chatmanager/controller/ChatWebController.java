package com.studilieferservice.chatmanager.controller;

import com.studilieferservice.chatmanager.group.Group;
import com.studilieferservice.chatmanager.group.GroupService;
import com.studilieferservice.chatmanager.message.ChatMessage;
import com.studilieferservice.chatmanager.user.User;
import com.studilieferservice.chatmanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Web Controller: accepts all web requests of this module and returns corresponding HTML pages.
 * All requests are accepted at ".../chat/".
 * @author Stefan Horst
 * @version 1.0
 */
@Controller
@RequestMapping("/chat")
public class ChatWebController {

    private final UserService userService;
    private final GroupService groupService;

    @Autowired
    public ChatWebController(UserService userService, GroupService groupService) {
        this.userService = userService;
        this.groupService = groupService;
    }

    /**
     * Web request (GET) for "/[Group-ID]": retrieves the page of the chat for the respective group
     * and user (who belongs to the group). The user is extracted from a browser cookie and thus not passed as a
     * parameter through the URL
     *
     * @param groupId The group-ID which is passed as a parameter through the URL
     * @return the HTML page "chat.html" which displays the chat
     */
    @GetMapping("/{groupId}")
    public ModelAndView getChatForGroup(@PathVariable String groupId, @CookieValue("useremail") String email, HttpServletRequest request) {
        if (!groupService.getGroup(groupId).getUsers().contains(userService.getUser(email)))
            return null;

        ModelAndView model = new ModelAndView("chat");

        model.addObject("group", groupService.getGroup(groupId));
        model.addObject("user", userService.getUser(email));
        model.addObject("messages", groupService.getAllChatMessagesOfGroup(groupService.getGroup(groupId)));

        //needed for POST request below
        model.addObject("chatMessage", new ChatMessage());
        model.addObject("link", request.getServerName());
        return model;
    }

    //only for testing without user cookie
    /*
     * Web request (GET) for "/[Group-ID]/[User-ID]": retrieves the page of the chat for the respective group
     * and user (who belongs to the group)
     *
     * @param groupId The group-ID which is passed as a parameter through the URL
     * @param userId The user-ID which is passed as a parameter through the URL
     * @return the HTML page "chat.html" which displays the chat
     */
    /*@GetMapping("/{groupId}/{userId}")
    public ModelAndView getChatForGroupAndUser(@PathVariable String groupId, @PathVariable String userId) {
        if (!groupService.getGroup(groupId).getUsers().contains(userService.getUser(userId)))
            return null;

        ModelAndView model = new ModelAndView("chat");

        model.addObject("group", groupService.getGroup(groupId));
        model.addObject("user", userService.getUser(userId));
        model.addObject("messages", groupService.getAllChatMessagesOfGroup(groupService.getGroup(groupId)));

        //needed for POST request below
        model.addObject("chatMessage", new ChatMessage());

        return model;
    }*/

    /**
     * Web request (POST) for "/addMessage": adds a message to the currently displayed chat of the group for the given user
     *
     * @param chatMessage object for the message which has been entered into the form
     * @param request Spring specific: for accessing the group-ID and user-ID which are stored in the body of the
     *                HTML page "chat.html"
     * @return a redirection to {@link #getChatForGroup(String, String, HttpServletRequest) getChatForGroupAndUser} with
     * the current group-ID and user-ID
     */
    @PostMapping("/addMessage")
    public RedirectView addMessageForUserAndGroup(@ModelAttribute(name = "chatMessage") ChatMessage chatMessage, HttpServletRequest request) {
        Group g = groupService.getGroup(request.getParameter("groupId"));
        User u = userService.getUser(request.getParameter("userId"));
        if (!g.getUsers().contains(u)) return new RedirectView("http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu");
        if(!chatMessage.getContent().equals("")) groupService.addTextChatMessageToGroupAndUser(u, g, chatMessage.getContent());

        return new RedirectView("http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + g.getId());
    }
}
