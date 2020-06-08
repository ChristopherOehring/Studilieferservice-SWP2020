package com.studilieferservice.groupmanager.controller;

import com.studilieferservice.groupmanager.controller.bodys.DeleteGroupBody;
import com.studilieferservice.groupmanager.controller.bodys.GetUserBody;
import com.studilieferservice.groupmanager.controller.bodys.CreateGroupBody;
import com.studilieferservice.groupmanager.controller.bodys.GroupAndUserBody;
import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.User;
import com.studilieferservice.groupmanager.service.GroupService;
import com.studilieferservice.groupmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

// TODO: 5/29/20 Is everything here only for testing purposes?
// TODO: 5/31/20 Should we have a method to remove admins, or do they need to be demoted first?
// TODO: 5/31/20 Should you be able to create multiple groups with the same name?
/**
 * Provides a api for the group-service at /api/group-service
 * @version 1.2 6/04/20
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
     * Adds a member to the Database of this Microservice.
     * Can be reached with a POST request at /api/group-service/user
     *
     * @param user A member in JsonFormat.
     *             Example:
     *             {
     *             "email":"max.mustermann@tu-ilmenau.de",
     *             "firstname":"Max",
     *             "lastname":"Mustermann",
     *             "username": "Moritz"
     *             }
     * @return returns the added member
     */
    @PostMapping(path = "/user")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        System.out.println("success");
        if(user.isValidEmailAddress(user.getEmail()) && user.isValidName(user.getFirstName()) && user.isValidName(user.getLastName())) {
            if (userService.findById(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User "+user.getEmail()+" already saved as "+user.getUserName()+" with the name "+user.getFirstName()+" "+user.getLastName());
            }
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        }
        //TODO causes 500, fix it
        else if (!user.isValidEmailAddress(user.getEmail()) || !user.isValidName(user.getFirstName()) || !user.isValidName(user.getLastName())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have to fill in both your first name and last name, also you may only use letters, dashes and spaces. Also, check your email address whether it matches the right from");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something wrong, check your input values");
        }
    }

    /**
     * Removes a member from the Database
     * Can be reached with a DELETE request at /api/group-service/user
     *
     * @param email the email adress of the member
     * @return returns "Operation successfull" if there was no error
     * , even if there was no member with that id //TODO isn't that supposed to be a bug?? ~ Manu 6/04/20
     */
    @DeleteMapping(path = "/user")
    public ResponseEntity<?> removeUser(@RequestBody String email) {
        if(userService.findById(email).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email address "+email+" found!");
        }
        System.out.println(email);
        userService.deleteUserById(email);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted user with email address "+email+" successfully!");
    }

    /**
     * Can be reached with a GET request at /api/group-service/user
     *
     * @param email the email of the member that should be returned as <b>raw text</b>
     * @return the member with that email, if there is one, null otherwise
     */
    @GetMapping(path = "/user")
    public ResponseEntity<?> getUser(@RequestBody GetUserBody email) {
        Optional<User> userOptional = userService.findById(email.getValue());
        if (userOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email address "+email+" found!");
        return ResponseEntity.status(HttpStatus.OK).body(userOptional.get());
    }

    /**
     * Can be reached with a GET request at /api/group-service/allUsers
     *
     * @return returns all Members in the database of the group-manager Microservice
     */
    @GetMapping(path = "/allUsers")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
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
     * @return The group, if it was successfully created, error message, if no user with the owner email could be found
     */
    @PostMapping(path = "/group")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupBody body) {
        Optional<User> ownerOptional = userService.findById(body.getOwnerEmail());
        if (ownerOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User "+body.getOwnerEmail()+" not found");
        Gruppe group = new Gruppe();
        group.setId(UUID.randomUUID().toString());
        group.setGroupName(body.getGroupName());
        group.setOwner(ownerOptional.get());
        groupService.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
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
    public ResponseEntity<?> removeGroup(@RequestBody DeleteGroupBody body){
        if(groupService.findById(body.getValue()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No group with group id "+body.getValue()+" found!");
        }
        groupService.deleteById(body.getValue());
        return ResponseEntity.status(HttpStatus.OK).body("Deleted group "+body.getValue());
    }

    /**
     * Can be used to get all groups
     * Can be reached with a GET request at /api/group-service/group
     * @return list of all groups in the database of this service
     */
    @GetMapping(path = "/group")
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findAll());
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
     * @return returns the group, including its new member, if group and user are present
     */
    @PutMapping(path = "/group/add")
    public ResponseEntity<?> addMember(@RequestBody GroupAndUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getGroupId());
        if (groupOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found!");
        Gruppe group = groupOptional.get();
        if (userService.findById(body.getEmail()).isPresent()) {
            User user = userService.findById(body.getEmail()).get();

            group.addMember(user);
            groupService.save(group);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully added user "+body.getEmail()+" to the group");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user was found in the repository. Try adding user "+body.getEmail()+" to the repository first");
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
    public ResponseEntity<?> removeMember(@RequestBody GroupAndUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getGroupId());
        if(groupOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found!");
        Gruppe group = groupOptional.get();
        if(userService.findById(body.getEmail()).isPresent()) {
            User user = userService.findById(body.getEmail()).get();

            group.removeMember(user);
            groupService.save(group);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully removed member "+body.getEmail()+"from the group");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email "+body.getEmail()+" was found in this group!");
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
    public ResponseEntity<?> promote(@RequestBody GroupAndUserBody body){
        Optional<Gruppe> groupOptional = groupService.findById(body.getGroupId());
        if(groupOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found!");
        Gruppe group = groupOptional.get();
        if(userService.findById(body.getEmail()).isPresent()) {
            User user = userService.findById(body.getEmail()).get();

            group.promote(user);
            groupService.save(group);
            return ResponseEntity.status(HttpStatus.OK).body("Promoted user "+body.getEmail());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email "+body.getEmail()+" was found in this group!");
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
    @PutMapping(path = "/group/demote")
    public ResponseEntity<?> demote(@RequestBody GroupAndUserBody body){
        Optional<Gruppe> groupOptional = groupService.findById(body.getGroupId());
        if(groupOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found!");
        Gruppe group = groupOptional.get();
        if(userService.findById(body.getEmail()).isPresent()) {
            User user = userService.findById(body.getEmail()).get();

            group.demote(user);
            groupService.save(group);
            return ResponseEntity.status(HttpStatus.OK).body("Demoted user "+body.getEmail());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email "+body.getEmail()+" was found in this group!");
    }
    
    // TODO: 5/31/20 as of now, admins cant be removed here, Should this be possible? - IMO only the owner should be able to remove admins ~ Manu 6/04/20
}
