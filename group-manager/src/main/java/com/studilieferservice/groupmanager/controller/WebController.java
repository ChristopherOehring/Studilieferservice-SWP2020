package com.studilieferservice.groupmanager.controller;

import com.studilieferservice.groupmanager.controller.bodys.CreationForm;
import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.User;
import com.studilieferservice.groupmanager.service.GroupService;
import com.studilieferservice.groupmanager.service.UserService;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * provides a web-controller at /web
 * A web controller returns html documents and is meant to be consumed via browser
 */

@RequestMapping("/web")
@Controller
public class WebController {

    private final GroupService groupService;
    private final UserService userService;


    @Autowired
    public WebController(GroupService groupService, UserService userService){
        this.groupService = groupService;
        this.userService = userService;
    }

    /**
     * get request on .../index invokes the index.html of the group-manager
     * @param model is used by thymeleaf in the html page
     * @return returns "index" which results in invocation of the index.html
     */
    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("groupList", groupService.findAll());
        model.addAttribute("creationForm", new CreationForm());
        return "index";
    }

    /**
     * Meant to be used by a webpage to create a new group. Will redirect to the index afterwards.
     * @param form contains all necessary information to create a group
     * @return redirects to /index
     * @throws JSONException Throws exception if the String body is not a valid JSON,
     * which should only happen if the group-name in the form is somehow invalid.
     * (I dont know if this is even possible)
     */
    @PostMapping("/save-group")
    public String saveGroupSubmission(@ModelAttribute CreationForm form) throws JSONException {
        Gruppe gruppe = new Gruppe();
        gruppe.setGroupName(form.getGroupName());
        gruppe.setId(UUID.randomUUID().toString());

        String[] users = form.getUsers()
                .replace(" ", "")
                .split(",");

        //TODO fixen ^^ -> firstname/lastname have to be replaced later on, also it might not be the best idea just to add a new user without saving in the user-repository
        for(String s: users){
            User u = new User(s, "fistname", "lastname", "username");
            gruppe.addMember(u);
            userService.save(u);
        }

        groupService.save(gruppe);

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

        return "redirect:index";
    }

    /**
     * redirects to the shopping list
     * @param request contains the group id in field "id"
     */
    @GetMapping("/getList")
    public RedirectView getList(HttpServletRequest request){

        String groupId = request.getParameter("id");

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8070/shoppingList/" + groupId);
        return redirectView;
    }

    /**
     * can be used to display all the groups related to a user
     * @param model is used by thymeleaf in the html page
     * @param email the email address of the user
     * @return returns "myGroups" which results in the invocation of the "myGroups" template
     */
    @GetMapping("/myGroups/{userId}")
    public String myGroups(Model model, @PathVariable("userId") String email) {
        model.addAttribute("groupsWhereMember", groupService.findAllWhereMember(email));
        model.addAttribute("groupsWhereAdmin", groupService.findAllWhereAdmin(email));
        model.addAttribute("groupsWhereOwner", groupService.findAllWhereOwner(email));
        return "myGroups";
    }

    /**
     * can be used to display all the groups related to a user
     * @param model is used by thymeleaf in the html page
     * @param email the email address of the user
     * @return returns "otherGroups" which results in the invocation of the "otherGroups" template
     */
    @GetMapping("otherGroups/{userId}")
    public String otherGroups(Model model, @PathVariable("userId") String email) {
        model.addAttribute("otherGroups", groupService.findAllOther(email));
        return "otherGroups";
    }
}
