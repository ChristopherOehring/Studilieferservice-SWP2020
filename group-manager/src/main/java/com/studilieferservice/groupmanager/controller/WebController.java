package com.studilieferservice.groupmanager.controller;

import com.studilieferservice.groupmanager.controller.bodys.CreationForm;
import com.studilieferservice.groupmanager.controller.bodys.GroupAndUserBody;
import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.Invite;
import com.studilieferservice.groupmanager.persistence.User;
import com.studilieferservice.groupmanager.service.GroupService;
import com.studilieferservice.groupmanager.service.InviteService;
import com.studilieferservice.groupmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;
import java.util.Optional;
import java.util.UUID;

/**
 * provides a web-controller at /web <br>
 * A web controller returns html documents and is meant to be consumed via browser
 * @author Christopher Oehring
 * @version 1.3 6/18/2020
 */

@RequestMapping("/web/groupmanager")
@Controller
public class WebController {

    private final GroupService groupService;
    private final UserService userService;
    private final String groupManagerSrc;
    private final InviteService inviteService;

    @Autowired
    public WebController(final @Value("${group-manager-src}") String groupManagerSrc,
                         GroupService groupService,
                         UserService userService,
                         InviteService inviteService) {
        this.groupService = groupService;
        this.userService = userService;
        this.groupManagerSrc = groupManagerSrc;
        this.inviteService = inviteService;
    }

    /**
     * get request on .../userMenu/{userEmail} invokes the userMenu.html of the group-manager
     * @param model is used by thymeleaf in the html page
     * @param email the PathVariable userEmail contains the email of the user who's menu should be displayed
     * @return returns "userMenu" which results in invocation of the userMenu.html
     */
    @GetMapping("/userMenu")
    public String userMenu(Model model,
                           @CookieValue("useremail") @Nullable String email,
                           final HttpServletResponse response,
                           HttpServletRequest request){

        if (email == null) return "redirect:noLogin";

        email = email.replace("%40", "@");
        System.out.println(email);
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) {
            System.out.println("Error 404: user \"" + email + "\" not found");
            model.addAttribute("subject", "user: \"" + email + "\"");
            return "customError404";
        }
        User user = optionalUser.get();
        model.addAttribute("link", request.getServerName());

        model.addAttribute("creationForm", new CreationForm());

        model.addAttribute("groupsWhereMember", groupService.findAllWhereMember(email));
        model.addAttribute("groupsWhereAdmin", groupService.findAllWhereAdmin(email));
        model.addAttribute("groupsWhereOwner", groupService.findAllWhereOwner(email));

        model.addAttribute("invites", user.getInvites());
        System.out.println(user.getInvites());
        model.addAttribute("user", email);

        model.addAttribute("groupManagerSrc", groupManagerSrc);
        return "userMenu";
    }

    /**
     * get request on .../groupMenu/{groupId} invokes the userMenu.html of the group-manager
     * @param model is used by thymeleaf in the html page
     * @param groupId the PathVariable groupId contains the id of the group who's menu should be displayed
     * @param email The value of the cookie "useremail"
     * @return returns "userMenu" which results in invocation of the userMenu.html
     */
    @GetMapping("/groupMenu/{groupId}")
    public String groupMenu(@PathVariable("groupId") String groupId,
                            @CookieValue("useremail") @Nullable String email,
                            Model model,
                            HttpServletRequest request) {

        if (email == null) return "redirect:/noLogin";

        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()){
            System.out.println("Error 404: group \"" + groupId + "\" not found");
            model.addAttribute("subject", "group: \"" + groupId + "\"");
            return "customError404";
        }
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) {
            System.out.println("Error 404: user \"" + email + "\" not found");
            model.addAttribute("subject", "user: \"" + email + "\"");
            return "customError404";
        }
        Gruppe gruppe = optionalGruppe.get();
        User user = optionalUser.get();
        System.out.println("line 120!");
        if(gruppe.getPermissions(user) == null) {
            model.addAttribute("subject", String.format("groupMenu of group %s for user %s", groupId, email));
            return "customError404";
        }

        model.addAttribute("groupId", groupId);
        model.addAttribute("owner", gruppe.getOwner());
        model.addAttribute("adminList", gruppe.getAdmins());
        model.addAttribute("memberList", gruppe.getMembers());
        model.addAttribute("link", request.getServerName());
        model.addAttribute("permission", gruppe.getPermissions(user));
        model.addAttribute("user", email);
        return "groupMenu";
    }

    /**
     * Meant to be used by a web page to create a new group. Will redirect to the groupMenu afterwards.
     * @param form contains all necessary information to create a group
     * @return redirects to /groupMenu in the composer
     * which should only happen if the group-name in the form is invalid.
     */
    @PostMapping("/save-group")
    public RedirectView saveGroupSubmission(
            @ModelAttribute CreationForm form,
            HttpServletRequest request) {
        RedirectView redirectView = new RedirectView();

        System.out.println("creating group " + form.getGroupName() + " with owner " + form.getUser());

        Gruppe group = new Gruppe();
        group.setGroupName(form.getGroupName());
        group.setId(UUID.randomUUID().toString());

        Optional<User> optionalUser = userService.findById(form.getUser());
        if(optionalUser.isEmpty()) {
            redirectView.setContextRelative(true);
            redirectView.setUrl("/customError404");
            return redirectView;
        }
        User user = optionalUser.get();

        group.setOwner(user);
        System.out.println(group);
        groupService.save(group);

        redirectView.setUrl("http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + group.getId());

        return redirectView;
    }

    /**
     * This method is used to switch to the composer because it is needed for composing the groupMenu
     * @param list the id of the Group
     * @return Returns a RedirectView that redirects to the groupMenu in the composer
     */
    @GetMapping("/groupMenuFwd")
    public RedirectView groupMenuFwd(@PathParam("list") String list,
                                     HttpServletRequest request){
        System.out.println(list);
        return new RedirectView("http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + list);
    }

    /**
     * redirects to the shopping list
     * @param request contains the group id in field "id"
     * @return a RedirectView that redirects to the shoppingList
     */
    @GetMapping("/getList")
    public RedirectView getList(HttpServletRequest request){

        String groupId = request.getParameter("id");
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://" + request.getServerName() + ":8070/shoppingList/" + groupId);
        return redirectView;
    }

    /**
     * this method is invoked when creating a new invite
     * @param body is used by thymeleaf in the html page
     * @return redirects to the groupMenu
     */
    @PostMapping("/invite")
    public String addInvite(@RequestBody GroupAndUserBody body) {
        Optional<User> optionalUser = userService.findById(body.getEmail());
        if(optionalUser.isEmpty()) System.out.println("No user with email "+body.getEmail()+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(body.getGroupId());
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+body.getGroupId()+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        inviteService.addInvite(new Invite(gruppe, user));

        return "redirect:groupMenu";//fixme
    }

    /**
     * used to accept an invite
     * @param body is used by thymeleaf in the html page
     * @return redirects to the userMenu
     */
    @PutMapping("/acceptInvite")
    public String acceptInvite(@RequestBody GroupAndUserBody body) {
        Optional<User> optionalUser = userService.findById(body.getEmail());
        if(optionalUser.isEmpty()) System.out.println("No user with email "+body.getEmail()+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(body.getGroupId());
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+body.getGroupId()+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        inviteService.removeInvite(new Invite(gruppe, user));
        gruppe.addMember(user);

        return "redirect:userMenu";
    }

    /**
     * used to decline an invite
     * @param body is used by thymeleaf in the html page
     * @return redirects to the userMenu
     */
    @PutMapping("/declineInvite")
    public String declineInvite(@RequestBody GroupAndUserBody body) {
        Optional<User> optionalUser = userService.findById(body.getEmail());
        if(optionalUser.isEmpty()) System.out.println("No user with email "+body.getEmail()+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(body.getGroupId());
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+body.getGroupId()+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        inviteService.removeInvite(new Invite(gruppe, user));

        return "redirect:userMenu";
    }
    //stürzt ab wenn nutzer keine invites hat
    //save-group keine WebPage, sondern nur Weiterleitung

    @GetMapping("/noLogin")
    public RedirectView noLogin(HttpServletRequest request){
        return new RedirectView("http://" + request.getServerName() + ":9080/web/usermanager/login");
    }
}
