package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.Invite;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        Gruppe gruppe = invite.getGroup();
        gruppe.removeInvite(invite);
        groupService.save(gruppe);
    }

    public void removeInvite(String groupId, String email){
        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+groupId+" was found!");
        Gruppe gruppe = optionalGruppe.get();
        System.out.println("Removing Invite: " + groupId + ", " + email);
        Optional<Invite> optionalInvite =
                gruppe.getInvites().stream()
                        .filter(invite -> invite.getUser().getEmail().equals(email))
                        .findAny();
        optionalInvite.ifPresent(this::removeInvite);
    }

    public boolean isValidInvite(Invite i) {
        Gruppe g = groupService.findById(i.getId().getGroupId()).orElse(null);
        if (g != null && g.getInvites().contains(i)) return true;
        return false;
    }
}