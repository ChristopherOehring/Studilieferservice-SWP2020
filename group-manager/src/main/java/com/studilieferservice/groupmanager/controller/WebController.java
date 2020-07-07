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
import javax.websocket.server.PathParam;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

//        if (gruppe.getDeliveryDate() != null)
//            model.addAttribute("date", gruppe.getDeliveryDate());
//        else
//            model.addAttribute("date", "-");
//
//        if (gruppe.getDeliveryPlace() != null)
//            model.addAttribute("address", gruppe.getDeliveryPlace());
//        else
//            model.addAttribute("address", Arrays.asList("-","","",""));

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

        group.setDeliveryDate(LocalDate.now().plusDays(2).format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        List<String> address = new ArrayList<>();
        address.add(user.getCity());
        address.add(user.getZip());

        //splits the single "street" string into the street and the house number as two separate strings
        if (!user.getStreet().matches(".*\\d.*")) {
            address.add(user.getStreet());
            address.add("");
        } else {
            String street = user.getStreet();
            StringBuilder number = new StringBuilder();

            int index;
            if (Character.isAlphabetic(street.charAt(street.length()-1))) {
                number.append(street.charAt(street.length() - 1));
                index = 2;
            } else
                index = 1;

            for (int i = street.length() - index; i > 0; i--)
            {
                if (Character.isDigit(street.charAt(i)))
                    number.insert(0, street.charAt(i));
                else if(Character.isAlphabetic(street.charAt(i)) || Character.isSpaceChar(street.charAt(i))) {
                    street = street.substring(0, i);
                    break;
                }
            }
            address.add(street);
            address.add(number.toString());
        }

        group.setDeliveryPlace(address);

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
    public String order(@ModelAttribute(name = "groupId") String groupId) {



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
