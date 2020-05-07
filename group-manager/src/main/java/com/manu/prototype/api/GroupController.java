package com.manu.prototype.api;

import com.manu.prototype.group.Group;
import com.manu.prototype.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("api/group")
@RestController
/**
 * Schnittstelle zwischen JSON Payload und Java System
 */
public class GroupController {
    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping(path = "{name}")
    public UUID createGroup(@PathVariable("name") String name) {
        return groupService.createGroup(name);
    }

    @GetMapping
    public List<Group> getAllGroups(){
        return groupService.getAllGroups();
    }

    @GetMapping(path = "{id}")
    public List<Group> getAllGroupsOfPerson(@PathVariable("id") String user){
        return groupService.getAllGroupsOfUser(user);
    }

    @DeleteMapping(path = "{id}")
    public void deleteGroupById(@PathVariable("id") UUID id){
        groupService.deleteGroup(id);
    }

    @PutMapping(path = "add")
    public void addUser(@RequestBody PutUserBody body){
        groupService.addUser(body.getUser(), body.getId());
    }

    @PutMapping(path = "remove")
    public void removeUser(@RequestBody PutUserBody body){
        groupService.removeUserFromGroup(body.getUser(), body.getId());
    }

    /*
    @PostMapping
    public void addUserToGroup(@RequestBody String user, Group group) {
        groupService.addUser(user, group);
    }

    @RequestMapping("/add-id")
    @PostMapping
    public void addUserToGroupByID(@RequestBody @JsonProperty("id") UUID id, Group group){
        groupService.addUserByID(id, group);
    }

    @GetMapping(path = "/users")
    public List<String> getAllUsersFromGroup(Group group) {
        return groupService.getAllUsers(group);
    }

    @GetMapping(path = "/name")
    public String getGroupName(Group group) {
        return groupService.getGroupName(group);
    }

    @GetMapping(path = "/{id}")
    public User getUserFromGroupByID(@PathVariable("id") UUID id, Group group) {
        return groupService.getUserByID(id, group).orElse(null);
    }

    @DeleteMapping(path ="/{id}")
    public void removeUserFromGroupByID(@PathVariable("id") UUID id, Group group) {
        groupService.removeUserFromGroupByID(id, group);
    }

    @RequestMapping("/test")
    @GetMapping
    public String test() {
        return "Hello Test :D";
    }
    */
}
