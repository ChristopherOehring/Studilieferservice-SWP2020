package com.studilieferservice.groupmanager.api;

import com.studilieferservice.groupmanager.api.bodys.DeleteGroupBody;
import com.studilieferservice.groupmanager.api.bodys.GetUserBody;
import com.studilieferservice.groupmanager.api.bodys.CreateGroupBody;
import com.studilieferservice.groupmanager.api.bodys.GroupAndUserBody;
import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.User;
import com.studilieferservice.groupmanager.service.GroupService;
import com.studilieferservice.groupmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// TODO: 5/29/20 Is everything here only for testing purposes?
// TODO: 5/31/20 Needs to be tested 
/**
 * Provides a api for the group-service at /api/group-service
 */
@RequestMapping("/api/group-service")
@RestController
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService) {
        this.groupService = groupService;
        this.userService = userService;
    }

//Users

    /**
     * Adds a user to the Database of this Microservice.
     * Can be reached with a POST request at /api/group-service/user
     *
     * @param user A user in JsonFormat.
     *             Example:
     *             {
     *             "email":"max.mustermann@tu-ilmenau.de",
     *             "firstname":"Max",
     *             "lastname":"Mustermann",
     *             "username": "Moritz"
     *             }
     * @return returns the added user
     */
    @PostMapping(path = "/user")
    public User addUser(@RequestBody User user) {
        System.out.println("success");
        return userService.save(user);
    }

    /**
     * Removes a user from the Database
     * Can be reached with a DELETE request at /api/group-service/user
     *
     * @param email the email adress of the user
     * @return returns "Operation successfull" if there was no error
     * , even if there was no user with that id
     */
    @DeleteMapping(path = "/user")
    public String removeUser(@RequestBody String email) {
        System.out.println(email);
        userService.deleteUserById(email);
        return "operation successful";
    }

    /**
     * Can be reached with a GET request at /api/group-service/user
     *
     * @param email the email of the user that should be returned as <b>raw text</b>
     * @return the user with that email, if there is one, null otherwise
     */
    @GetMapping(path = "/user")
    public User getUser(@RequestBody GetUserBody email) {
        Optional<User> userOptional = userService.findUser(email.getValue());
        if (userOptional.isEmpty()) return null;
        return userOptional.get();
    }

    /**
     * Can be reached with a GET request at /api/group-service/allUsers
     *
     * @return returns all users in the database of the group-manager Microservice
     */
    @GetMapping(path = "/allUsers")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

//Groups

    /**
     * Can be used to create a group
     * Can be reached with a POST request at /api/group-service/group
     *
     * @param body A GroupCreationBody wich contains the groupName and the email of the owner
     *             Example:
     *             {
     *             "groupName":"SWP",
     *             "email":"max.mustermann@tu-ilmenau.de"
     *             }
     * @return The group, if it was successfully created, null if no user with the owner email could be found
     */
    @PostMapping(path = "/group")
    public Gruppe createGroup(@RequestBody CreateGroupBody body) {
        Optional<User> ownerOptional = userService.findUser(body.getOwnerEmail());
        if (ownerOptional.isEmpty()) return null;
        Gruppe group = new Gruppe();
        group.setId(UUID.randomUUID().toString());
        group.setGroupName(body.getGroupName());
        group.setOwner(ownerOptional.get());
        return groupService.save(group);
    }

    /**
     * Can be Used to delete a group
     * Can be reached with a DELETE request at /api/group-service/group
     * @param body contains the "groupId" as json
     *             Example:
     *             {
     *                  "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163"
     *             }
     * @return returns true if the operation was successful
     */
    @DeleteMapping(path = "/group")
    public boolean removeGroup(@RequestBody DeleteGroupBody body){
        return groupService.deleteById(body.getValue());
    }

    /**
     * Can be used to get all groups
     * Can be reached with a GET request at /api/group-service/group
     * @return list of all groups in the database of this service
     */
    @GetMapping(path = "/group")
    public List<Gruppe> getAllGroups() {
        return groupService.findAll();
    }

    /**
     * Can be used to add a user (from the database) to the list of members of a specific Group
     * Can be reached with a PUT request at /api/group-service/group/add
     * Parameters are sent via a JSON body in the http request
     * @param body A GroupAndUserBody, wich contains the groupId and the identifying email of the user
     *      *             Example:
     *      *             {
     *      *                  "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163",
     *      *                  "email":"max.mustermann@tu-ilmenau.de"
     *      *             }
     * @return returns the group, including its new member
     */
    @PutMapping(path = "/group/add")
    public Gruppe addMember(@RequestBody GroupAndUserBody body) {
        Gruppe group = groupService.findById(body.getGroupId()).get();
        User user = userService.findUser(body.getEmail()).get();
        
        group.addUser(user);
        return groupService.save(group);
    }

    /**
     * Removes a user from a group
     * Can be reached with a PUT request at /api/group-service/group/remove
     * @param body A GroupAndUserBody, wich contains the groupId and the identifying email of the user
     *      *             Example:
     *      *             {
     *      *                  "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163",
     *      *                  "email":"max.mustermann@tu-ilmenau.de"
     *      *             }
     * @return the group, as it was saved in the database
     */
    @PutMapping(path = "/group/remove")
    public Gruppe removeMember(@RequestBody GroupAndUserBody body) {
        Gruppe group = groupService.findById(body.getGroupId()).get();
        User user = userService.findUser(body.getEmail()).get();

        group.removeUser(user);
        return groupService.save(group);
    }

    /**
     * Promotes a member of a group to admin
     * Can be reached with a PUT request at /api/group-service/group/promote
     * @param body A GroupAndUserBody, wich contains the groupId and the identifying email of the user
     *      *             Example:
     *      *             {
     *      *                  "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163",
     *      *                  "email":"max.mustermann@tu-ilmenau.de"
     *      *             }
     * @return the group, as it was saved in the database
     */
    @PutMapping(path = "/group/promote")
    public Gruppe promote(@RequestBody GroupAndUserBody body){
        Gruppe group = groupService.findById(body.getGroupId()).get();
        User user = userService.findUser(body.getEmail()).get();
    
        group.promote(user);
        return groupService.save(group);
    }

    /**
     * Demotes a admin of a group to member
     * Can be reached with a PUT request at /api/group-service/group/promote
     * @param body A GroupAndUserBody, wich contains the groupId and the identifying email of the user
     *      *             Example:
     *      *             {
     *      *                  "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163",
     *      *                  "email":"max.mustermann@tu-ilmenau.de"
     *      *             }
     * @return the group, as it was saved in the database
     */
    @PutMapping(path = "/group/promote")
    public Gruppe demote(@RequestBody GroupAndUserBody body){
        Gruppe group = groupService.findById(body.getGroupId()).get();
        User user = userService.findUser(body.getEmail()).get();

        group.demote(user);
        return groupService.save(group);
    }
    
    // TODO: 5/31/20 as of now, admins cant be removed here, Should this be possible?
}
