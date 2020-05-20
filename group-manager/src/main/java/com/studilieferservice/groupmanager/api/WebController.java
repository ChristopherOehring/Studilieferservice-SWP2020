package com.studilieferservice.groupmanager.api;

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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.UUID;

@RequestMapping("/web/group")
@Controller
public class WebController {

    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public WebController(GroupService groupService, UserService userService){
        this.groupService = groupService;
        this.userService = userService;
    }

    @GetMapping("/index")
    public String index(Model model) {
        model.addAttribute("groupList", groupService.findAll());
        model.addAttribute("creationForm", new CreationForm());
        return "index";
    }

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
            User u = new User(s, "fistname", "lastname");
            gruppe.addUser(u);
            userService.save(u);
        }

        groupService.save(gruppe);

        //following code is for telling the shopping-list-manager via POST to create a shopping list for the new group
        String urlPOST = "http://localhost:8070/shoppingList/create";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"groupId\": \""+gruppe.getId()+"\", \"groupName\": \""+form.groupName+"\" }";

        JSONObject jsonObject= new JSONObject(body);

        HttpEntity request = new HttpEntity(jsonObject.toString(), headers);

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(urlPOST, request , String.class );

        return "redirect:index";
    }

    private ModelAndView build(List<Gruppe> gruppen) {

        var result = new ModelAndView("index");
        result.addObject("groupList", gruppen);
        System.out.println(gruppen);
        return result;
    }

    @GetMapping("/getList")
    public RedirectView getList(HttpServletRequest request){

        String groupId = request.getParameter("id");

        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8070/shoppingList/" + groupId);
        return redirectView;
    }
}
