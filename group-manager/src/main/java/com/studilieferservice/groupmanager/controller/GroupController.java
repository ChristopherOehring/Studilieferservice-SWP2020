package com.studilieferservice.groupmanager.controller;

import com.studilieferservice.groupmanager.controller.bodys.GroupDeliveryBody;
import com.studilieferservice.groupmanager.controller.bodys.DeleteGroupBody;
import com.studilieferservice.groupmanager.controller.bodys.GetUserBody;
import com.studilieferservice.groupmanager.controller.bodys.CreateGroupBody;
import com.studilieferservice.groupmanager.controller.bodys.GroupAndUserBody;
import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.Invite;
import com.studilieferservice.groupmanager.persistence.User;
import com.studilieferservice.groupmanager.service.GroupService;
import com.studilieferservice.groupmanager.service.InviteService;
import com.studilieferservice.groupmanager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;
import java.util.UUID;

// TODO: 5/29/20 Is everything here only for testing purposes?   -   IDK, thought this will be needed for the final product ~ Manu 6/23/20
// TODO: 5/31/20 Should we have a method to remove admins, or do they need to be demoted first?   -   demoted first ~ Manu 6/23/20
// TODO: 5/31/20 Should you be able to create multiple groups with the same name?   -   IMO yes, cause you can distinguish the groups because of their UUID ~ 6/23/20

/**
 * Provides an api for the group-service at /api/group-service
 *
 * @author Christopher Oehring
 * @author Manuel Jirsak
 * @version 2.7 6/24/20 //TODO: 6/18/20 shouldn't it be version "2".5, as it is the completely rewritten version of the prototype? ~ Manu
 */
@RequestMapping("/api/group-service")
@RestController
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;
    private final InviteService inviteService;

    @Autowired
    public GroupController(GroupService groupService, UserService userService, InviteService inviteService) {
        this.groupService = groupService;
        this.userService = userService;
        this.inviteService = inviteService;
    }

//Users

    /**
     * Adds a member to the Database of this Microservice. <br>
     * Can be reached with a POST request at /api/group-service/user
     *
     * @param user A member in JsonFormat, see also {@link User}
     *             <pre>
     *                         Example:
     *                         {
     *                         "email":"max.mustermann@tu-ilmenau.de",
     *                         "firstname":"Max",
     *                         "lastname":"Mustermann",
     *                         "username": "Moritz"
     *                         }
     *                         </pre>
     * @return returns the added member
     */
    @PostMapping(path = "/user")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        if (user.isValidEmailAddress(user.getEmail()) && user.isValidName(user.getFirstName()) && user.isValidName(user.getLastName()) && !user.getUserName().isBlank()) {
            if (userService.findById(user.getEmail()).isPresent()) {
                User user1 = userService.findById(user.getEmail()).get();
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + user1.getEmail() + " already saved as " + user1.getUserName() + " with the name " + user1.getFirstName() + " " + user1.getLastName());
            }
            userService.save(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } else if (!user.isValidEmailAddress(user.getEmail()) || !user.isValidName(user.getFirstName()) || !user.isValidName(user.getLastName()) || user.getUserName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have to fill in both your first name and last name, also you may only use letters, dashes and spaces. Also, check your email address whether it matches the right form and make sure you've chosen an username");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Something wrong, check your input values");
        }
    }

    /**
     * Removes a member from the Database <br>
     * Can be reached with a DELETE request at /api/group-service/user <br>
     * Body has to contain the e-mail in JSON-Syntax, e.g. if the e-mail is "max.mustermann@tu-ilmenau.de", the body must contain { "email":"max.mustermann@tu-ilmenau.de" }, an example with explanation can be found at {@link GetUserBody}
     *
     * @param email the email address of the member
     * @return returns "OK" if there was no error and the user was deleted, otherwise returns "NOT_FOUND" if there is no user
     */
    @DeleteMapping(path = "/user")
    public ResponseEntity<?> removeUser(@RequestBody GetUserBody email) {
        if (userService.findById(email.getValue()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email address " + email.getValue() + " found!");
        }
        System.out.println(email.getValue());
        userService.deleteUserById(email.getValue());
        return ResponseEntity.status(HttpStatus.OK).body("Deleted user with email address " + email.getValue() + " successfully!");
    }

    /**
     * Can be reached with a GET request at /api/group-service/user <br>
     * Body has to contain the e-mail in JSON-Syntax, e.g. if the e-mail is "max.mustermann@tu-ilmenau.de", the body must contain { "email":"max.mustermann@tu-ilmenau.de" }, an example with explanation can be found at {@link GetUserBody}
     *
     * @param email the email of the member that should be returned as <b>raw text</b> //TODO what does that last part "raw text" mean? return value is the whole user, input is json syntax ~ Manu 6/23/20
     * @return the member with that email, if there is one, "NOT_FOUND"-response entity otherwise
     */
    @GetMapping(path = "/user")
    public ResponseEntity<?> getUser(@RequestBody GetUserBody email) {
        Optional<User> userOptional = userService.findById(email.getValue());
        if (userOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email address " + email.getValue() + " found!");
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
     * Can be used to create a group. <br>
     * Can be reached with a POST request at /api/group-service/group.
     *
     * @param body A GroupCreationBody wich contains the groupName and the email of the owner, to see an explanation, go to {@link CreateGroupBody}
     *             <pre>
     *                         Example:
     *                         {
     *                         "groupName":"SWP",
     *                         "email":"max.mustermann@tu-ilmenau.de"
     *                         }
     *                         </pre>
     * @return The group, if it was successfully created, error message, if no user with the owner email could be found
     */
    @PostMapping(path = "/group")
    public ResponseEntity<?> createGroup(@RequestBody CreateGroupBody body) {
        Optional<User> ownerOptional = userService.findById(body.getOwnerEmail());
        if (ownerOptional.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User " + body.getOwnerEmail() + " not found");
        if (body.getGroupName()==null || body.getGroupName().isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You have to set a group name");
        }
        Gruppe group = new Gruppe();
        group.setId(UUID.randomUUID().toString());
        group.setGroupName(body.getGroupName());
        group.setOwner(ownerOptional.get());
        groupService.save(group);
        return ResponseEntity.status(HttpStatus.CREATED).body(group);
    }

    /**
     * Can be Used to delete a group <br>
     * Can be reached with a DELETE request at /api/group-service/group
     *
     * @param body contains the "groupId" as json, see {@link DeleteGroupBody} for more information
     *             <pre>
     *                         Example:
     *                         {
     *                         "groupId":"81fce800-3cf9-4583-8ed6-56326c1d3163"
     *                         }
     *                         </pre>
     * @return returns short message if the operation was successful, otherwise "NOT_FOUND"-response entity
     */
    @DeleteMapping(path = "/group")
    public ResponseEntity<?> removeGroup(@RequestBody DeleteGroupBody body) {
        if (groupService.findById(body.getValue()).isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No group with group id " + body.getValue() + " found!");
        }
        groupService.deleteById(body.getValue());
        return ResponseEntity.status(HttpStatus.OK).body("Deleted group " + body.getValue());
    }

    /**
     * Can be used to get all groups <br>
     * Can be reached with a GET request at /api/group-service/group
     *
     * @return list of all groups in the database of this service
     */
    @GetMapping(path = "/group")
    public ResponseEntity<?> getAllGroups() {
        return ResponseEntity.status(HttpStatus.OK).body(groupService.findAll());
    }

    /**
     * Can be used to add a user (from the database) to the list of members of a specific Group <br>
     * Can be reached with a PUT request at /api/group-service/group/add <br>
     * Parameters are sent via a JSON body in the http request
     *
     * @param body A {@link GroupAndUserBody}, which contains the groupId and the identifying email of the user
     * @return returns the group, including its new member, if group and user are present
     */
    @PutMapping(path = "/group/add")
    public ResponseEntity<?> addMember(@RequestBody GroupAndUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getGroupId());
        if (groupOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found!");
        Gruppe group = groupOptional.get();
        if (userService.findById(body.getEmail()).isPresent()) {
            User user = userService.findById(body.getEmail()).get();
            if (group.getMembers().contains(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + body.getEmail() + " is already a Member of this group");
            }
            if (group.getAdmins().contains(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + body.getEmail() + " is already an Admin of this group");
            }
            if (group.getOwner().equals(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + body.getEmail() + " is already the Owner of this group");
            }

            group.addMember(user);
            groupService.save(group);
            return ResponseEntity.status(HttpStatus.OK).body("Successfully added user " + body.getEmail() + " to the group");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No such user was found in the repository. Try adding user " + body.getEmail() + " to the repository first");
    }

    /**
     * Removes a user from a group <br>
     * Can be reached with a PUT request at /api/group-service/group/remove
     *
     * @param body A {@link GroupAndUserBody}, which contains the groupId and the identifying email of the user
     * @return the group, as it was saved in the database
     */
    @PutMapping(path = "/group/remove")
    public ResponseEntity<?> removeMember(@RequestBody GroupAndUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getGroupId());
        if (groupOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found!");
        Gruppe group = groupOptional.get();
        if (userService.findById(body.getEmail()).isPresent()) {
            User user = userService.findById(body.getEmail()).get();
            if (group.getMembers().contains(user)) {
                group.removeMember(user);
                groupService.save(group);
                return ResponseEntity.status(HttpStatus.OK).body("Successfully removed member " + body.getEmail() + " from the group");
            }
            if (group.getAdmins().contains(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot remove admin " + body.getEmail() + " from the group, can only remove members");
            }
            if (group.getOwner().equals(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot remove owner " + body.getEmail() + " from the group, a group must not exist without an owner");
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email " + body.getEmail() + " was found in this group!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unknown user " + body.getEmail() + "!");
    }

    /**
     * Promotes a member of a group to admin <br>
     * Can be reached with a PUT request at /api/group-service/group/promote
     *
     * @param body A {@link GroupAndUserBody}, which contains the groupId and the identifying email of the user
     * @return the group, as it was saved in the database if successful, otherwise you'll either get "NOT_FOUND" if user or group don't exist or "BAD_REQUEST" if you're trying to promote an admin or the owner
     */
    @PutMapping(path = "/group/promote")
    public ResponseEntity<?> promote(@RequestBody GroupAndUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getGroupId());
        if (groupOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found!");
        Gruppe group = groupOptional.get();
        if (userService.findById(body.getEmail()).isPresent()) {
            User user = userService.findById(body.getEmail()).get();
            if (group.getMembers().contains(user)) {
                group.promote(user);
                groupService.save(group);
                return ResponseEntity.status(HttpStatus.OK).body("Promoted user " + body.getEmail());
            }
            if (group.getAdmins().contains(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + body.getEmail() + " is already an admin of this group");
            }
            if (group.getOwner().equals(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + body.getEmail() + " is the owner of the group and therefore already has admin rights");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email " + body.getEmail() + " was found in this group!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unknown user " + body.getEmail() + "!");
    }

    /**
     * Demotes a admin of a group to member <br>
     * Can be reached with a PUT request at /api/group-service/group/promote
     *
     * @param body A {@link GroupAndUserBody}, which contains the groupId and the identifying email of the user
     * @return A ResponseEntity, indicating whether the demotion was successful or not (either because group or user don't exist or because you're trying to demote an user or the owner
     */
    @PutMapping(path = "/group/demote")
    public ResponseEntity<?> demote(@RequestBody GroupAndUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getGroupId());
        if (groupOptional.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Group not found!");
        Gruppe group = groupOptional.get();
        if (userService.findById(body.getEmail()).isPresent()) {
            User user = userService.findById(body.getEmail()).get();
            if (group.getAdmins().contains(user)) {
                group.demote(user);
                groupService.save(group);
                return ResponseEntity.status(HttpStatus.OK).body("Demoted user " + body.getEmail());
            }
            if (group.getMembers().contains(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + body.getEmail() + " is already a member and cannot be demoted further");
            }
            if (group.getOwner().equals(user)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Cannot demote " + body.getEmail() + ", as it is the owner of the group");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email " + body.getEmail() + " was found in this group!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Unknown user " + body.getEmail() + "!");
    }

    /**
     * Creates an Invite <br>
     * Can be reached with a POST request at /api/group-service/invite
     *
     * @param body A {@link GroupAndUserBody}, which contains the groupId and the identifying email of the user
     * @return A ResponseEntity, indicating whether the Creation of the Invite was successfully created or not (that's the case if there is no matching group or user - "NOT_FOUND", or if the user already is part of the group - "BAD_REQUEST")
     */
    @PostMapping(path = "/invite")
    public ResponseEntity<?> addInvite(@RequestBody GroupAndUserBody body) {
        Optional<User> optionalUser = userService.findById(body.getEmail());
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email " + body.getEmail() + " was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(body.getGroupId());
        if (optionalGruppe.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No group with id " + body.getGroupId() + " was found!");
        Gruppe gruppe = optionalGruppe.get();

        if (gruppe.getMembers().contains(user) || gruppe.getAdmins().contains(user) || gruppe.getOwner().equals(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User " + body.getEmail() + " is already part of the group and cannot be invited twice");
        }

        Invite invite = new Invite(gruppe, user);
        inviteService.addInvite(invite);

        System.out.println(Arrays.toString(user.getInvites().toArray()));
        return ResponseEntity.status(HttpStatus.OK).body("Added Invite from " + gruppe.getGroupName() + " to " + user.getUserName());
    }

    /**
     * Deletes an Invite <br>
     * Can be reached with a DELETE request at /api/group-service/invite
     *
     * @param body A {@link GroupAndUserBody - click to see an explanation and an example}, which contains the groupId and the identifying email of the user
     * @return A ResponseEntity, indicating whether the Deletion was successfully created or not (that's the case if there is no matching group, user or invitation - "NOT_FOUND", or if the user already has accepted the invitation)
     */
    @DeleteMapping(path = "/invite")
    public ResponseEntity<?> removeInvite(@RequestBody GroupAndUserBody body) {
        Optional<User> optionalUser = userService.findById(body.getEmail());
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email " + body.getEmail() + " was found!");
        User user = optionalUser.get();

        Optional<Gruppe> optionalGruppe = groupService.findById(body.getGroupId());
        if (optionalGruppe.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No group with id " + body.getGroupId() + " was found!");
        Gruppe gruppe = optionalGruppe.get();

        Optional<Invite> optionalInvite =
                user.getInvites().stream()
                        .filter(invite -> invite.getGroup().equals(gruppe) && invite.getUser().equals(user))
                        .findFirst();
        if (optionalInvite.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No corresponding invite was found!");
        inviteService.removeInvite(optionalInvite.get());

        System.out.println(Arrays.toString(user.getInvites().toArray()));
        return ResponseEntity.status(HttpStatus.OK).body("Removed Invite from " + gruppe.getGroupName() + " to " + user.getUserName());
    }

    /**
     * Use to see all invites of a specific user <br>
     * Can be reached with a GET request at api/group-service/invite
     *
     * @param body common {@link GetUserBody} containing the email of the user you want to see the invites of
     * @return a list of invites of the provided user; if the user cannot be found: "NOT_FOUND"
     */
    @GetMapping(path = "/invite")
    public ResponseEntity<?> getInvitesOfUser(@RequestBody GetUserBody body) {
        Optional<User> optionalUser = userService.findById(body.getValue());
        if (optionalUser.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No user with email " + body.getValue() + " was found!");
        User user = optionalUser.get();
        return ResponseEntity.status(HttpStatus.OK).body(user.getInvites());
    }

    /**
     * Changes the delivery data of a group <br>
     * Can be reached with a POST request at api/group-service/deliveryData <br>
     * You can have the delivery data returned to you (along with all group information) when with a GET request at api/group-service/group
     *
     * @param body A {@link GroupDeliveryBody - you can find an example there}, which contains the group id and the place to deliver to (consisting of city-name, zip code, street and house number)
     * @return A response entity containing the updated group (if something changed, otherwise just "nothing changed") or explaining what went wrong (400: some data is invalid; 404: group cannot be found)
     */
    @PostMapping(path = "/deliveryData")
    public ResponseEntity<?> setDeliveryData(@RequestBody GroupDeliveryBody body) {
        Optional<Gruppe> optionalGruppe = groupService.findById(body.getGroupId());
        if (optionalGruppe.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No group with id " + body.getGroupId() + " was found!");
        Gruppe gruppe = optionalGruppe.get();
        if (body.getDate() == null || body.getPlace().get(0) == null || body.getPlace().get(1) == null || body.getPlace().get(2) == null || body.getPlace().get(3) == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some entries are null");
        }
        if (body.getDate().isEmpty() || body.getPlace().get(0).isBlank() || body.getPlace().get(1).isBlank() || body.getPlace().get(2).isBlank() || body.getPlace().get(3).isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Missing data in date or place");
        }
        if (!gruppe.isValidDate(body.getDate())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Delivery date is invalid! - " + body.getPlace().get(0) + ", " + body.getPlace().get(1) + ", " + body.getPlace().get(2) + ", " + body.getPlace().get(3) + ", " + "  -  " + body.getDate());
        }
        if (gruppe.getDeliveryPlace().equals(body.getPlace()) && gruppe.getDeliveryDate().equals(body.getDate())) {
            return ResponseEntity.status(HttpStatus.OK).body("Nothing changed");
        }
        if (!body.getPlace().get(0).isBlank() && !body.getPlace().get(1).isBlank() && !body.getPlace().get(2).isBlank() && !body.getPlace().get(3).isBlank()) {
            if (body.getPlace().get(0).matches("[a-z A-Z-À-ÿ]*") && body.getPlace().get(1).matches("[0-9]*") && body.getPlace().get(2).matches("[a-z A-Z-À-ÿ]*") && body.getPlace().get(3).matches("[0-9]*[a-zA-Z]?")) {
                gruppe.setDeliveryPlace(body.getPlace());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update delivery data - Check that your address details are correct: City and street only contains letters, zip code only contains letters and house number contains numbers and maximum one letter");
            }
        }
        if (gruppe.isValidDate(body.getDate())) {
            gruppe.setDeliveryDate(body.getDate());
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Could not update delivery data - Check that your date matches the valid form of DD/MM/YYYY");
        }
        //gruppe.setDeliveryDate(body.getDate());
        groupService.save(gruppe);
        return ResponseEntity.status(HttpStatus.OK).body(gruppe);
    }

    /**
     * Changes the owner of the group, afterwards, the former owner is saved as admin
     * Can be reached with a POST request at api/group-service/group/owner
     *
     * @param body A {@link GroupAndUserBody}, which contains the groupId and the identifying email of the user
     * @return A response entity indicating, whether the change was successful (200, former owner is now an admin) or not (404, either group or user weren't found) - if the nothing changed, it is indicated as 200 OK
     */
    @PostMapping(path = "/group/owner")
    public ResponseEntity<?> setOwner(@RequestBody GroupAndUserBody body) {
        Optional<Gruppe> optionalGruppe = groupService.findById(body.getGroupId());
        if (optionalGruppe.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No group with id " + body.getGroupId() + " was found!");
        Gruppe gruppe = optionalGruppe.get();
        for (User u : gruppe.getMembers()) {
            if (u.getEmail().equals(body.getEmail())) {
                User owner = gruppe.getOwner();
                gruppe.setOwner(u);
                gruppe.addAdmin(owner);
                groupService.save(gruppe);
                return ResponseEntity.status(HttpStatus.OK).body("User " + body.getEmail() + " is now the owner of this group");
            }
        }
        for (User a : gruppe.getAdmins()) {
            if (a.getEmail().equals(body.getEmail())) {
                User owner = gruppe.getOwner();
                gruppe.setOwner(a);
                gruppe.addAdmin(owner);
                groupService.save(gruppe);
                return ResponseEntity.status(HttpStatus.OK).body("User " + body.getEmail() + " is now the owner of this group");
            }
        }
        if (gruppe.getOwner().getEmail().equals(body.getEmail())) {
            return ResponseEntity.status(HttpStatus.OK).body("User " + body.getEmail() + " is already the owner of this group");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User " + body.getEmail() + " is not a member of this group");
    }

}
