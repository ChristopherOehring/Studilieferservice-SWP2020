package com.studilieferservice.groupmanager.api;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("api/group")
@RestController
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(final GroupService groupService) {
        this.groupService = Objects.requireNonNull(groupService);
    }

    @PostMapping(path = "{name}")
    public Gruppe createGroup(@PathVariable("name") String name) {
        Gruppe gruppe = new Gruppe();
        gruppe.setGroupName(name);
        gruppe.setId(UUID.randomUUID().toString());

        gruppe.addUser("test@testmail.com");

        return groupService.save(gruppe);
    }

    @GetMapping
    public List<Gruppe> getAll(){
        return groupService.findAll();
    }

    @GetMapping(path = "{id}")
    public List<Gruppe> getAllGroupsOfPerson(@PathVariable("id") String userId){
        List<Gruppe> groups = groupService.findAll();
        List<Gruppe> ret = new ArrayList<>();
        for(Gruppe g: groups) {
            if(g.getUsers().contains(":" + userId + ":")){
                ret.add(g);
            }
        }
        return ret;
    }

    @DeleteMapping(path = "{id}")
    public void deleteGroupById(@PathVariable("id") String id){
        groupService.deleteById(id);
    }

    @PutMapping(path = "add")
    public String addUser(@RequestBody PutUserBody body){
        Optional<Gruppe> groupOptional = groupService.findById(body.getId());
        if (groupOptional.isEmpty()) return "Group not found";
        Gruppe g = groupOptional.get();

        g.addUser(body.getUser());
        groupService.save(g);
        return "added";
    }

    @PutMapping(path = "remove")
    public void removeUser(@RequestBody PutUserBody body){
        Optional<Gruppe> groupOptional = groupService.findById(body.getId());
        if (groupOptional.isEmpty()) return;
        Gruppe g = groupOptional.get();
        g.removeUser(body.getUser());
        groupService.save(g);
    }
}
