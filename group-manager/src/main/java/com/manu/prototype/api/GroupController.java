package com.manu.prototype.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.manu.prototype.group.JpaGroupRepository;
import com.manu.prototype.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RequestMapping("api/group")
@RestController
public class GroupController {
    private final JpaGroupRepository groupRepository;

    @Autowired
    public GroupController(final JpaGroupRepository groupRepository, final ObjectMapper objectMapper) {
        this.groupRepository = Objects.requireNonNull(groupRepository);
    }

    @PostMapping(path = "{name}")
    public Group createGroup(@PathVariable("name") String name) {
        Group group = new Group(name);
        group.setId(UUID.randomUUID());
        return groupRepository.save(group);
    }

    @GetMapping
    public List<Group> getAll(){
        return groupRepository.findAll();
    }

    @GetMapping(path = "{id}")
    // TODO: 07.05.2020 change loop to Collection.removelf
    public List<Group> getAllGroupsOfPerson(@PathVariable("id") String user){
        List<Group> groups = groupRepository.findAll();
        for(Group g: groups) {
            if(!g.getUsers().contains(user)) {
                groups.remove(g);
            }
        }
        return groups;
    }

    @DeleteMapping(path = "{id}")
    public void deleteGroupById(@PathVariable("id") UUID id){
        groupRepository.delete(id);
    }

    @PutMapping(path = "add")
    public boolean addUser(@RequestBody PutUserBody body){
        Group g = groupRepository.findOne(body.getId());
        return g.addUser(body.getUser());
    }

    @PutMapping(path = "remove")
    public boolean removeUser(@RequestBody PutUserBody body){
        return groupRepository.findOne(body.getId()).removeUser(body.getUser());
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
