package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Invite;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Should be used to add Invites to the Database
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */
@Service
public class InviteService {

    private GroupService groupService;
    private UserService userService;

    @Autowired
    public InviteService(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

    public void addInvite(Invite invite){
        User user = invite.getUser();
        user.addInvite(invite);
        userService.save(user);
    }

    public void removeInvite(Invite invite){
        User user = invite.getUser();
        user.removeInvite(invite);
        userService.save(user);
    }
}