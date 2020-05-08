package com.manu.prototype.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manu.prototype.persistence.Gruppe;
import com.manu.prototype.persistence.JpaGroupRepository;
import com.manu.prototype.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RequestMapping("api/group")
@RestController
public class GroupController {
    private final JpaGroupRepository groupRepository;

    @Autowired
    public GroupController(final JpaGroupRepository groupRepository, final ObjectMapper objectMapper) {
        this.groupRepository = Objects.requireNonNull(groupRepository);
    }

    @PostMapping(path = "{name}")
    public Gruppe createGroup(@PathVariable("name") String name) {
        Gruppe gruppe = new Gruppe();
        gruppe.setGroupName(name);
        gruppe.setId(UUID.randomUUID().toString());

        gruppe.addUser("test@testmail.com");

        return groupRepository.save(gruppe);
    }

    @GetMapping
    public List<Gruppe> getAll(){
        return groupRepository.findAll();
    }

    @GetMapping(path = "{id}")
    public List<Gruppe> getAllGroupsOfPerson(@PathVariable("id") String userId){
        List<Gruppe> groups = groupRepository.findAll();
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
        groupRepository.deleteById(id);
    }

    @PutMapping(path = "add")
    public String addUser(@RequestBody PutUserBody body){
        Optional<Gruppe> groupOptional = groupRepository.findById(body.getId());
        if (groupOptional.isEmpty()) return "Group not found";
        Gruppe g = groupOptional.get();

        g.addUser(body.getUser());
        groupRepository.save(g);
        return "added";
    }

    @PutMapping(path = "remove")
    public void removeUser(@RequestBody PutUserBody body){
        Optional<Gruppe> groupOptional = groupRepository.findById(body.getId());
        if (groupOptional.isEmpty()) return;
        Gruppe g = groupOptional.get();
        g.removeUser(body.getUser());
        groupRepository.save(g);
    }

    /*
    @PostMapping
    public void addUserToGroup(@RequestBody String user, Gruppe group) {
        groupService.addUser(user, group);
    }

    @RequestMapping("/add-id")
    @PostMapping
    public void addUserToGroupByID(@RequestBody @JsonProperty("id") UUID id, Gruppe group){
        groupService.addUserByID(id, group);
    }

    @GetMapping(path = "/users")
    public List<String> getAllUsersFromGroup(Gruppe group) {
        return groupService.getAllUsers(group);
    }

    @GetMapping(path = "/name")
    public String getGroupName(Gruppe group) {
        return groupService.getGroupName(group);
    }

    @GetMapping(path = "/{id}")
    public User getUserFromGroupByID(@PathVariable("id") UUID id, Gruppe group) {
        return groupService.getUserByID(id, group).orElse(null);
    }

    @DeleteMapping(path ="/{id}")
    public void removeUserFromGroupByID(@PathVariable("id") UUID id, Gruppe group) {
        groupService.removeUserFromGroupByID(id, group);
    }

    @RequestMapping("/test")
    @GetMapping
    public String test() {
        return "Hello Test :D";
    }
    */
}
