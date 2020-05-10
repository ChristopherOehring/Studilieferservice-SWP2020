package com.manu.prototype;

import com.manu.prototype.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.manu.prototype.persistence.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class WebController {

    private GroupService groupService;

    @Autowired
    public WebController(GroupService groupService){
        this.groupService = groupService;
    }

    @GetMapping("/index")
    public ModelAndView index(){
        return build(groupService.findAll());
    }

    private ModelAndView build(List<Gruppe> gruppen) {

        var result = new ModelAndView("index");
        result.addObject("groupList", gruppen);
        System.out.println(gruppen);
        return result;
    }
}
