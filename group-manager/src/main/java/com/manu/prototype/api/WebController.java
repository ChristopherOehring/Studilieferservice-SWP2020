package com.manu.prototype.api;

import com.manu.prototype.api.CreationForm;
import com.manu.prototype.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.manu.prototype.persistence.*;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.UUID;

@RequestMapping("/web/group")
@Controller
public class WebController {

    private GroupService groupService;

    @Autowired
    public WebController(GroupService groupService){
        this.groupService = groupService;
    }

    @GetMapping("/index")
    public String index(Model model){
        model.addAttribute("groupList", groupService.findAll());
        model.addAttribute("creationForm", new CreationForm());
        return "index";
    }

    @PostMapping("/save-group")
    public String saveGroupSubmission(@ModelAttribute CreationForm form){
        Gruppe gruppe = new Gruppe();
        gruppe.setGroupName(form.getGroupName());
        gruppe.setId(UUID.randomUUID().toString());

        String[] users = form.getUsers()
                .replace(" ", "")
                .split(",");

        for(String s: users){
            gruppe.addUser(s);
        }

        groupService.save(gruppe);

        return "redirect";
    }

    private ModelAndView build(List<Gruppe> gruppen) {

        var result = new ModelAndView("index");
        result.addObject("groupList", gruppen);
        System.out.println(gruppen);
        return result;
    }
}
