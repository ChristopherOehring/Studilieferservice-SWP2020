package com.studilieferservice.groupmanager.controller;

import com.studilieferservice.groupmanager.controller.bodys.CreationForm;
import com.studilieferservice.groupmanager.controller.bodys.GroupAndUserBody;
import com.studilieferservice.groupmanager.controller.bodys.TestForm;
import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.Invite;
import com.studilieferservice.groupmanager.persistence.User;
import com.studilieferservice.groupmanager.service.GroupService;
import com.studilieferservice.groupmanager.service.InviteService;
import com.studilieferservice.groupmanager.service.UserService;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.http.HttpHeaders;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * provides a web-controller at /web <br>
 * A web controller returns html documents and is meant to be consumed via browser
 * @author Christopher Oehring
 * @version 1.3 6/18/2020
 */

// TODO Figure out how to properly handle errors here

@RequestMapping("/web")
@Controller
public class WebController {

    private final GroupService groupService;
    private final UserService userService;
    private String groupManagerSrc;
    private InviteService inviteService;

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

    @GetMapping("/test")
    public String test(Model model, @RequestHeader @Nullable HttpHeaders header) {
        List<String> sessionValues;
        if(header != null) {
            sessionValues = header.allValues("x-rd-test");
        } else {
            sessionValues = new ArrayList<>();
        }
        model.addAttribute("sessionMessages", sessionValues);
        model.addAttribute("form", new TestForm());
        System.out.println(sessionValues);
        return "test";
    }

    @GetMapping("/persistMessage")
    public String persistMessage(/*@ModelAttribute TestForm form,*/@RequestParam("msg") String msg, HttpServletResponse response) {
        //response.addHeader("x-rd-test", form.getMsg());
        System.out.println(msg);
        return "redirect:test";
    }

    @PostMapping("/destroy")
    public String destroySession(HttpServletRequest request) {
        request.getSession().invalidate();
        return "redirect:/web/test";
    }

    /**
     * get request on .../userMenu/{userEmail} invokes the userMenu.html of the group-manager
     * @param model is used by thymeleaf in the html page
     * @param email the PathVariable userEmail contains the email of the user who's menu should be displayed
     * @return returns "userMenu" which results in invocation of the userMenu.html
     */
    @GetMapping("/userMenu/{userEmail}")
    public String userMenu(Model model,
                        @PathVariable("userEmail") String email,
                           final HttpServletResponse response){

        response.addHeader("x-uic-stylesheet", "/groupmanager/style.css");

        email = email.replace("%40", "@");
        System.out.println(email);
        Optional<User> optionalUser = userService.findById(email);
        if(optionalUser.isEmpty()) {
            System.out.println("Error 404: user \"" + email + "\" not found");
            model.addAttribute("subject", "user: \"" + email + "\"");
            return "nichtStandarderror404";
        }
        User user = optionalUser.get();

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
     * get request on .../groupMenu/{userEmail} invokes the userMenu.html of the group-manager
     * @param model is used by thymeleaf in the html page
     * @param groupId the PathVariable groupId contains the id of the group who's menu should be displayed
     * @return returns "userMenu" which results in invocation of the userMenu.html
     */
    @GetMapping("/groupMenu/{groupId}")
    public String groupMenu(Model model, @PathVariable("groupId") String groupId) {
        model.addAttribute("groupId", groupId);

        return "groupMenu";
    }

    /**
     * Meant to be used by a webpage to create a new group. Will redirect to the index afterwards.
     * @param form contains all necessary information to create a group
     * @return redirects to /index
     * @throws JSONException Throws exception if the String body is not a valid JSON,
     * which should only happen if the group-name in the form invalid.
     */
    @PostMapping("/save-group")
    public String saveGroupSubmission(Model model, @ModelAttribute CreationForm form) throws JSONException {
        Gruppe group = new Gruppe();
        group.setGroupName(form.getGroupName());
        group.setId(UUID.randomUUID().toString());

        Optional<User> optionalUser = userService.findById(form.getUser());
        if(optionalUser.isEmpty()) {
            model.addAttribute("subject", "user: \"" + form.getUser() + "\"");
            return "nichtStandarderror404";
        }
        User user = optionalUser.get();

        group.setOwner(user);

        groupService.save(group);

        /*
        //following code is for telling the shopping-list-manager via POST to create a shopping list for the new group
        String urlPOST = "http://localhost:8070/shoppingList/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"groupId\": \""+gruppe.getId()+"\", \"groupName\": \""+form.groupName+"\" }";

        JSONObject jsonObject= new JSONObject(body);

        //Raw use of parameterized class. Intellij doesnt like this:
        HttpEntity request = new HttpEntity(jsonObject.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(urlPOST, request , String.class );
        */

        return "redirect:groupMenu/" + group.getId();
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
        redirectView.setUrl("http://localhost:8070/shoppingList/" + groupId);
        return redirectView;
    }

    /*
    /**
     * called through a browser by the creationForm from groupCreator(...)
     * @param form the object containing the input from the form
     * @return redirects back to the groupCreator page
     */
    /*@PostMapping("/newGroup")
    public String newGroup(@ModelAttribute CreationForm form, Model model) {
        System.out.println(form);
        Gruppe group = new Gruppe();
        group.setGroupName(form.getGroupName());

        Optional<User> userOptional = userService.findById(form.getUser());
        //TODO: how do we handle this error?
        if (userOptional.isEmpty()) {
            model.addAttribute("subject", "user: \"" + form.getUser() + "\"");
            return "error";
        }
        User user = userOptional.get();
        group.setOwner(user);
        groupService.save(group);

//TODO: and where do we go afterwards?
        return "redirect:groupMenu/" + group.getId();
    }
    /*

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

    //JUST FOR DEBUG
    @GetMapping("/test2")
    public String testSite(Model model) {
        return "groupMenu";
    }

    @GetMapping("/test3/{userEmail}")
    public String testSite2(Model model, @PathVariable("userEmail") String email) {
        return "userMenu";
    }

    //stürzt ab wenn nutzer keine invites hat
    //save-group keine WebPage, sondern nur Weiterleitung
}
