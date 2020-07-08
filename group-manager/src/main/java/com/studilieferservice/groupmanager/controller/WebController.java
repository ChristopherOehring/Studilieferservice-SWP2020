package com.studilieferservice.groupmanager.controller;

import com.studilieferservice.groupmanager.controller.bodys.CreationForm;
import com.studilieferservice.groupmanager.controller.bodys.GroupAndUserBody;
import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.Invite;
import com.studilieferservice.groupmanager.persistence.User;
import com.studilieferservice.groupmanager.service.GroupService;
import com.studilieferservice.groupmanager.service.InviteService;
import com.studilieferservice.groupmanager.service.UserService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;
import java.util.*;

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
        model.addAttribute("groupAndUserBody", new GroupAndUserBody());

        model.addAttribute("groupsWhereMember", groupService.findAllWhereMember(email));
        model.addAttribute("groupsWhereAdmin", groupService.findAllWhereAdmin(email));
        model.addAttribute("groupsWhereOwner", groupService.findAllWhereOwner(email));

        model.addAttribute("invites", user.getInvites());
        System.out.println(user.getInvites());
        model.addAttribute("user", email);
        model.addAttribute("userName", user.getUserName());

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
            return "redirect:groupMenuFwd";
        }
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) {
            System.out.println("Error 404: user \"" + email + "\" not found");
            model.addAttribute("subject", "user: \"" + email + "\"");
            return "redirect:groupMenuFwd";
        }
        Gruppe gruppe = optionalGruppe.get();
        User user = optionalUser.get();
        if(gruppe.getPermissions(user) == null) {
            System.out.println(String.format("groupMenu of group %s for user %s not found", groupId, email));
            return "redirect:groupMenuFwd";
        }

        model.addAttribute("groupAndUserBody", new GroupAndUserBody());
        model.addAttribute("thisGroupId", groupId);
        model.addAttribute("owner", gruppe.getOwner());
        model.addAttribute("adminList", gruppe.getAdmins());
        model.addAttribute("memberList", gruppe.getMembers());
        model.addAttribute("pendingInvites", gruppe.getInvites());
        model.addAttribute("link", request.getServerName());
        model.addAttribute("permission", gruppe.getPermissions(user));
        model.addAttribute("currUser", user);
        model.addAttribute("currGroup", gruppe);
        model.addAttribute("hasAcceptedDate", gruppe.hasAcceptedDeliveryDate(user));
        model.addAttribute("mayOrder", gruppe.hasEveryoneAcceptedDeliveryDate());
        model.addAttribute("address", gruppe.getDeliveryPlace());
        model.addAttribute("date", gruppe.getDeliveryDate());
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

        if (form.groupName.isBlank()){
            redirectView.setUrl("http://" + request.getServerName() + ":9010/web/groupmanager/userMenu/");
            return redirectView;
        }

        System.out.println("creating group " + form.getGroupName() + " with owner " + form.getUser());

        Optional<User> optionalUser = userService.findById(form.getUser());
        if(optionalUser.isEmpty()) {
            redirectView.setContextRelative(true);
            redirectView.setUrl("/customError404");
            return redirectView;
        }
        User user = optionalUser.get();
        String groupId = groupService.createGroup(form.getGroupName(), user);

        redirectView.setUrl("http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + groupId);

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
        if (list != null)
            return new RedirectView("http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + list);
        else
            return new RedirectView("http://" + request.getServerName() + ":9010/web/groupmanager/userMenu/");
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
     * this method is used to kick a user from the groupMenu
     * @param groupId The Id of the Group from which the user should be kicked
     * @param email The email of the user to be kicked
     * @return redirects to the groupMenu
     */
    @PostMapping("/kick")
    public String kick(@ModelAttribute(name = "groupId") String groupId, @ModelAttribute(name = "email") String email) {
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) System.out.println("No user with email "+email+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+groupId+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        gruppe.removeMember(user);
        groupService.save(gruppe);

        return "redirect:groupMenuFwd?list=" + groupId;
    }

    /**
     * this method is used to promote a user from the groupMenu
     * @param groupId The Id of the Group in which the user should be promoted
     * @param email The email of the user to be promoted
     * @return redirects to the groupMenu
     */
    @PostMapping("/promote")
    public String promote(@ModelAttribute(name = "groupId") String groupId, @ModelAttribute(name = "email") String email) {
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) System.out.println("No user with email "+email+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+groupId+" was found!");
        Gruppe gruppe = optionalGruppe.get();
        System.out.println("Promoting " +email+ " in " + groupId);
        gruppe.promote(user);
        groupService.save(gruppe);

        return "redirect:groupMenuFwd?list=" + groupId;
    }

    /**
     * this method is used to demote a user from the groupMenu
     * @param groupId The Id of the Group in which the user should be demoted
     * @param email The email of the user to be demoted
     * @return redirects to the groupMenu
     */
    @PostMapping("/demote")
    public String demote(@ModelAttribute(name = "groupId") String groupId, @ModelAttribute(name = "email") String email) {
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) System.out.println("No user with email "+email+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+groupId+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        System.out.println("Demoting " +email+ " in " + groupId);
        gruppe.demote(user);
        groupService.save(gruppe);

        return "redirect:groupMenuFwd?list=" + groupId;
    }

    /**
     * this method is invoked when creating a new invite from the groupMenu
     * @param groupId The Id of the Group to which the user should be invited
     * @param email The email of the user to be invited
     * @return redirects to the groupMenu
     */
    @PostMapping("/invite")
    public String addInvite(@ModelAttribute(name = "groupId") String groupId, @ModelAttribute(name = "email") String email) {
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) {
            System.out.println("No user with email "+email+" was found!");
            return "redirect:groupMenuFwd?list=" + groupId;
        }
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+groupId+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        inviteService.addInvite(new Invite(gruppe, user));

        return "redirect:groupMenuFwd?list=" + groupId;
    }

    /**
     * used to accept an invite
     * @param groupId The Id of the Group in which the user was invited
     * @param email The email of the invited user
     * @return redirects to the userMenu
     */
    @PostMapping("/acceptInvite")
    public String acceptInvite(@ModelAttribute(name = "groupId") String groupId, @ModelAttribute(name = "email") String email) {
        System.out.println("accept invite:" + email + ", " + groupId);

        inviteService.removeInvite(groupId, email);

        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) System.out.println("No user with email "+email+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+groupId+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        gruppe.addMember(user);
        groupService.save(gruppe);
        return "redirect:userMenu";
    }

    /**
     * used to decline an invite from the userMenu
     * @param groupId The Id of the Group in which the user was invited
     * @param email The email of the invited user
     * @return redirects to the userMenu
     */
    @PostMapping("/declineInvite")
    public String declineInvite(@ModelAttribute(name = "groupId") String groupId,
                                @ModelAttribute(name = "email") String email) {
        System.out.println("decline invite:" + email + ", " + groupId);

        inviteService.removeInvite(groupId, email);

        return "redirect:userMenu";
    }

    /**
     * used to withdraw an invite from the groupMenu
     * @param groupId The Id of the Group in which the user was invited
     * @param email The email of the invited user
     * @return redirects to the userMenu
     */
    @PostMapping("/withdrawInvite")
    public String withdrawInvite(@ModelAttribute(name = "groupId") String groupId,
                                @ModelAttribute(name = "email") String email) {
        System.out.println("decline invite:" + email + ", " + groupId);

        inviteService.removeInvite(groupId, email);

        return "redirect:groupMenuFwd?list=" + groupId;
    }

    @PostMapping("/confirmDate")
    public String confirmDate(@ModelAttribute(name = "groupId") String groupId,
                              @ModelAttribute(name = "email") String email) {
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) System.out.println("No user with email "+email+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+groupId+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        gruppe.acceptDeliveryDate(user);
        groupService.save(gruppe);

        return "redirect:groupMenuFwd?list=" + groupId;
    }

    @PostMapping("/unconfirmDate")
    public String unconfirmDate(@ModelAttribute(name = "groupId") String groupId,
                                @ModelAttribute(name = "email") String email) {
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) System.out.println("No user with email "+email+" was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(groupId);
        if (optionalGruppe.isEmpty()) System.out.println("No group with id "+groupId+" was found!");
        Gruppe gruppe = optionalGruppe.get();

        gruppe.acceptNoLongerDeliveryDate(user);
        groupService.save(gruppe);

        return "redirect:groupMenuFwd?list=" +groupId;
    }

    @PostMapping("/order")
    public String order(@ModelAttribute(name = "groupId") String groupId,
                        @ModelAttribute(name = "email") String email,
                        @ModelAttribute(name = "date") String date,
                        @ModelAttribute(name = "address") ArrayList<String> addressList) {

        Gruppe group = groupService.findById(groupId).orElseThrow();
        User user = userService.findById(email).orElseThrow();

        String address = addressList.get(1) + " " + addressList.get(0).replace("[", "") + ", "
                + addressList.get(2) + " " + addressList.get(3).replace("]", "");

        group.acceptNoLongerDeliveryDate(userService.findById(group.getOwner().getEmail()).orElseThrow());
        for (User u : group.getAdmins()) {
            group.acceptNoLongerDeliveryDate(userService.findById(u.getEmail()).orElseThrow());
        }
        for (User u : group.getMembers()) {
            group.acceptNoLongerDeliveryDate(userService.findById(u.getEmail()).orElseThrow());
        }
        groupService.save(group);

        //REST request to chat-manager to add info message
        String urlRequest = "http://chat-manager:8040/chat/addOrder";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"groupId\": \"" + group.getId() + "\", \"userId\": \"" + user.getEmail() + "\", \"date\": \""
                + date + "\", \"address\": \"" + address + "\" }";

        try {
            JSONObject jsonObject = new JSONObject(body);

            HttpEntity<String> restRequest = new HttpEntity<>(jsonObject.toString(), headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(urlRequest, restRequest , String.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //REST request to shopping-list-manager to get complete shopping list
        urlRequest = "http://shopping-list-manager:8070/shoppingList/getComplete/" + groupId;

        String completeShoppingList = "error";
        try {
            HttpEntity<String> restRequest = new HttpEntity<>(headers);

            RestTemplate restTemplate = new RestTemplate();
            HttpEntity<String> response = restTemplate.exchange(urlRequest, HttpMethod.GET, restRequest, String.class);

            completeShoppingList = "{\n\t\"date\": \""+date+"\",\n\t\"address\": \""+address+"\",";
            completeShoppingList += Objects.requireNonNull(response.getBody()).substring(1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(completeShoppingList); //complete shopping list of group

        //REST request to shopping-list-manager to clear shopping lists
        urlRequest = "http://shopping-list-manager:8070/shoppingList/removeAllProductsForGroup";

        body = "{ \"groupId\": \"" + group.getId() + "\" }";

        try {
            JSONObject jsonObject = new JSONObject(body);

            HttpEntity<String> restRequest = new HttpEntity<>(jsonObject.toString(), headers);

            RestTemplate restTemplate = new RestTemplate();
            restTemplate.put(urlRequest, restRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:groupMenuFwd?list=" +groupId;
    }

    @GetMapping("/noLogin")
    public RedirectView noLogin(HttpServletRequest request){
        return new RedirectView("http://" + request.getServerName() + ":9080/web/usermanager/login");
    }

    @PostMapping("/changeDeliveryDate")
    public String changeDeliveryDate(HttpServletRequest request) {
        String groupId = request.getParameter("groupId");
        String date = request.getParameter("deliveryDate");

        //maybe dont do this validation for demo
        if (!groupService.findById(groupId).orElseThrow().isValidDate(date) &&
                !groupService.findById(groupId).orElseThrow().getDeliveryDate().isEmpty())
            return "redirect:http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + groupId;

        Gruppe group = groupService.findById(groupId).orElseThrow();
        group.setDeliveryDate(date.trim());
        groupService.save(group);

        return "redirect:http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + groupId;
    }

    @PostMapping("/changeDeliveryAddress")
    public String changeDeliveryAddress(HttpServletRequest request) {
        String groupId = request.getParameter("groupId");
        String address = request.getParameter("deliveryAddress");

        ArrayList<String> list = new ArrayList<>();
        Collections.addAll(list, address.split(","));
        list.replaceAll(String::trim);

        //maybe dont do this validation for demo
        if (list.size() != 4 || list.get(0).isBlank() || list.get(1).isBlank() || list.get(2).isBlank() || list.get(3).isBlank()
                || !list.get(1).matches("[a-z A-Z-À-ÿ]*") || !list.get(0).matches("[0-9]*")
                || !list.get(2).matches("[a-z A-Z-À-ÿ]*") || !list.get(3).matches("[0-9]*[a-zA-Z]?"))
            return "redirect:http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + groupId;

        //if (list.size() == 4) //can be removed when validation above is active
            Collections.swap(list, 0, 1); //only this line is necessary
        //else {
        //    if (list.size() > 4)
        //        list = new ArrayList<>(list.subList(0, 3));
        //    else {
        //        while (list.size() < 4)
        //            list.add("");
        //    }
        //}

        Gruppe group = groupService.findById(groupId).orElseThrow();
        group.setDeliveryPlace(list);
        groupService.save(group);

        return "redirect:http://" + request.getServerName() + ":9000/web/groupmanager/groupMenu/" + groupId;
    }
}
