package com.studilieferservice.groupmanager.api;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.User;
import com.studilieferservice.groupmanager.service.GroupService;
import com.studilieferservice.groupmanager.service.UserService;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.*;

/**
 * Rest-Controller: provides a rest api at /api/group
 * @version 1.0
 */

@RequestMapping("api/group")
@RestController
public class GroupController {
    private final GroupService groupService;
    private final UserService userService;

    /**
     * Classic constructor method, no explanation needed
     * @param groupService Instance of GroupService for configuring/saving groups in JpaGroupRepository extending JpaRepository<Gruppe, String>
     * @param userService Instance of UserService for configuring/saving users in JpaUserRepository extending JpaRepository<User, String>
     */
    @Autowired
    public GroupController(final GroupService groupService, UserService userService) {
        this.groupService = Objects.requireNonNull(groupService);
        this.userService = userService;
    }

    /**
     * Creating a group via POST at api/group/{name} with only one user in the beginning, determined by a body in JSON-format like that:
     *      {
     *           "id":"group-id",
     *           "user":"user-mail-address",
     *           "firstname":"some first name",
     *           "lastname":"some last name"
     *      }
     * Requesting user is promoted to owner and admin
     * There is always only one owner and never less
     * @param name Name of the Group you want to create
     * @return new group gets saved as a new group in GroupRepository with a randomly generated UUID
     */
    @PostMapping(path = "{name}")
    @ManyToMany(fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    public Gruppe createGroup(@PathVariable("name") String name, @RequestBody PutUserBody body) {
        Gruppe gruppe = new Gruppe();
        gruppe.setGroupName(name);
        gruppe.setId(UUID.randomUUID().toString());
        //Testzwecke
        //Owner o = new Owner("test@testmail.com","Testo", "Testson"); //RIP Testo Testson
        User owner = new User(body.getUserID(), body.getUserFirstName(), body.getUserLastName(), body.getUserName());
        gruppe.setOwner(owner);
        if (userService.findUser(owner.getEmail()).isEmpty()) {
            userService.save(owner);
        }
        return groupService.save(gruppe);
    }

    /**
     * Use via GET at /api/group/, gives you all existing groups with information such as group-name, group-id and users
     * @return List of existing Groups
     */
    @GetMapping
    public List<Gruppe> getAll(){
        return groupService.findAll();
    }

    /**
     * Use via GET at /api/group/{user-id}, gives you all existing groups of a specific user with information such as group-name, group-id and users
     * WARNING: squared runtime due to checking of ALL members of ALL groups
     * @param userId User-ID is its E-Mail-Address
     * @return List of existing Groups, where user has joined
     */
    // TODO: 5/21/20 use a user-service to make this prettier and faster
    @GetMapping(path = "{id}")
    public List<Gruppe> getAllGroupsOfPerson(@PathVariable("id") String userId){
        List<Gruppe> groups = groupService.findAll();
        List<Gruppe> ret = new ArrayList<>();
        for(Gruppe g: groups) {
            for (User u: g.getUsers()) {
                if (u.getEmail().equals(userId)) {
                    ret.add(g);
                    break;
                }
            }
        }
        return ret;
    }

    /**
     * Delete a group via DELETE at /api/group/{id}
     * @param id UUID of group you want to delete
     * @return just an informative string whether this group got deleted right now or didn't exist and ... that's it
     */
    @DeleteMapping(path = "{id}")
    public String deleteGroupById(@PathVariable("id") String id){
        if(groupService.findById(id).isPresent()) {
            groupService.deleteById(id);
            return "Deleted group";
        } else
        return "Group not found";
    }

    /**
     * Add an user to a specific group, use via PUT at "api/group/add" with a body in JSON-format like that:
     *      {
     * 	        "id":"group-id",
     * 	        "user":"user-mail-address",
     * 	        "firstname":"some first name",
     * 	        "lastname":"some last name"
     *      }
     * @param body JSON-Payload containing 4 Strings, used to determine user and group
     * @return 4 Cases:
     *          Group not found: something is wrong with the group-ID - maybe a group with this ID does not exist anymore or you've got a typo
     *          User already member of group: yeah, you can't have a user twice
     *          User already admin of group: there is already an admin with this ID, he cannot be both user and admin
     *          User added to repository and to group: user was not in group before and is now added to its group in the groupRepository and to the userRepository (if user didn't exist before, otherwise it will say "User added to group")
     */
    @PutMapping(path = "add")
    public String addUser(@RequestBody PutUserBody body){
        Optional<Gruppe> groupOptional = groupService.findById(body.getId());
        if (groupOptional.isEmpty()) return "Group not found";
        if (body.getUser().isValidEmailAddress(body.getUserID())) {
            Gruppe g = groupOptional.get();
            for (User u : g.getUsers()) {
                if (u.getEmail().equals(body.getUserID())) {
                    return "User already member of group";
                }
            }
            for (User a : g.getAdmins()) {
                if (a.getEmail().equals(body.getUserID())) {
                    return "User already admin of group";
                }
            }
            User u = new User(body.getUserID(), body.getUserFirstName(), body.getUserLastName(), body.getUserName());
            g.addUser(u);
            if (userService.findUser(body.getUserID()).isEmpty()) {
                userService.save(u);
                return "User added to repository and to group";
            }
            groupService.save(g);
            return "User added to group";
        }
        return "Could not add user, something is wrong with your email address (non-empty, may only contain letters, numbers, dots, periods or dashes) or your name (non-empty, may only contain letters, dashes and spaces)";
    }

    /**
     * Remove an user from a specific group, use via PUT at "api/group/remove" with a body in JSON-format like that:
     *      {
     *           "id":"group-id",
     *           "user":"user-mail-address",
     *           "firstname":"some first name",
     *           "lastname":"some last name"
     *      }
     * @param body JSON-Payload containing 4 Strings, but only "id" and "user" are important (where "user" is the mail-address)
     * @return 4 Cases:
     *          Group not found: something is wrong with the group-ID - maybe a group with this ID does not exist anymore or you've got a typo
     *          Group is now empty and will be deleted: The last remaining member was removed, so the group is useless and got deleted
     *          User removed: Congratulations, that's what you tried to achieve - the user is removed from the groupRepository (but still remains in the userRepository because he still might be in other groups)
     *          No user was found in this group with this id, but since You tried to remove him, it doesn't matter anymore: No user was found in this group with this id, but since You tried to remove him, it doesn't matter anymore
     */
    @PutMapping(path = "remove")
    public String removeUser(@RequestBody PutUserBody body){
        Optional<Gruppe> groupOptional = groupService.findById(body.getId());
        if (groupOptional.isEmpty()) return "Group not found";
        Gruppe g = groupOptional.get();
        for (User u: g.getUsers()) {
            if(u.getEmail().equals(body.getUserID())) {
                g.removeUser(u);
                groupService.save(g);
                if(g.getUsers().isEmpty() && g.getAdmins().isEmpty()) {
                    groupService.deleteById(body.getId());
                    return "Group is now empty and will be deleted";
                }
                return "User removed";
            }
        }
        for (User a: g.getAdmins()) {
            if(a.getEmail().equals(body.getUserID())) {
                return "You can't remove an admin";
            }
        }
        return "No user was found in this group with this id, but since You tried to remove him, it doesn't matter anymore";
    }

    @PutMapping(path = "update")
    public String updateUser(@RequestBody PutUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getId());
        if (groupOptional.isEmpty()) return "Group not found";
        if (body.getUser().isValidEmailAddress(body.getUserID())) {
            Gruppe g = groupOptional.get();
            for (User u : g.getUsers()) {
                if (u.getEmail().equals(body.getUserID())) {
                    g.updateUser(u, body.getUser());
                    groupService.save(g);
                    return "Updated user " + body.getUserFirstName() + " " + body.getUserLastName();
                }
            }
            for (User a : g.getAdmins()) {
                if (a.getEmail().equals(body.getUserID())) {
                    g.updateUser(a, body.getUser());
                    groupService.save(g);
                    return "Updated admin " + body.getUserFirstName() + " " + body.getUserLastName();
                }
            }
            if (g.getOwner().getEmail().equals(body.getUserID())) {
                g.updateUser(g.getOwner(), body.getUser());
                return "Updated owner " + body.getUserFirstName() + " " + body.getUserLastName();
            }
            return "User " + body.getUserID() + " not found!";
        }
        return "Could not update user, something is wrong with your email address (non-empty, may only contain letters, numbers, dots, periods or dashes) or your name (non-empty, may only contain letters, dashes and spaces)";
    }

    /**
     * User PUT at /api/group/add_admin with a body in JSON-format like that:
     *      {
     *           "id":"group-id",
     *           "user":"user-mail-address",
     *           "firstname":"some first name",
     *           "lastname":"some last name"
     *      }
     * @param body JSON-Payload containing 4 Strings, but only "id" and "user" are important (where "user" is the mail-address)
     * @return 3 Cases:
     *          Group not found: something is wrong with the group-ID - maybe a group with this ID does not exist anymore or you've got a typo
     *          User <firstname> <lastname> is now an admin: success, this user got promoted to admin and is no longer shown in the user list but in the admin list
     *          User <User-ID> is not a member of this group or already an admin: you have to add the user first, before promoting him to admin, or he might already be an admin
     */
    @PutMapping(path = "add_admin")
    public String addAdmin(@RequestBody PutUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getId());
        if (groupOptional.isEmpty()) return "Group not found";
        Gruppe g = groupOptional.get();
        if (body.getUser().isValidEmailAddress(body.getUserID())) {
            for (User u : g.getUsers()) {
                if (u.getEmail().equals(body.getUserID())) {
                    g.addAdmin(body.getUser());
                    g.removeUser(u);
                    groupService.save(g);
                    return "User " + u.getFirstName() + " " + u.getLastName() + " is now an admin";
                }
            }
            return "User " + body.getUserID() + " is not a member of this group or already an admin";
        }
        return "Could not promote user to admin, something is wrong with your email address (non-empty, may only contain letters, numbers, dots, periods or dashes) or your name (non-empty, may only contain letters, dashes and spaces)";
    }

    /**
     * User PUT at /api/group/add_admin with a body in JSON-format like that:
     *      {
     *           "id":"group-id",
     *           "user":"user-mail-address",
     *           "firstname":"some first name",
     *           "lastname":"some last name"
     *      }
     * @param body JSON-Payload containing 4 Strings, but only "id" and "user" are important (where "user" is the mail-address)
     * @return 4 Cases:
     *          Group not found: something is wrong with the group-ID - maybe a group with this ID does not exist anymore or you've got a typo
     *          Owner cannot be degraded: There is always one owner and never less, you can't degrade him
     *          Admin <firstname> <lastname> removed: This admin got removed from the admin list and is now a mere user of this group
     *          There is no admin <User-ID> in this group: The admin you want to remove does not exist in this group or is already just a mere user
     */
    @PutMapping(path = "degrade_admin")
    public String degradeAdmin(@RequestBody PutUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getId());
        if (groupOptional.isEmpty()) return "Group not found";
        Gruppe g = groupOptional.get();
        for (User a: g.getAdmins()) {
            if(a.getEmail().equals(body.getUserID())) {
                if(g.isOwner(a)) {
                    return "Owner cannot be degraded";
                }
                g.removeAdmin(a);
                g.addUser(a);
                groupService.save(g);
                return "Admin "+a.getFirstName()+" "+a.getLastName()+" removed";
            }
        }
        return "There is no admin "+body.getUserID()+" in this group";
    }

    @PutMapping(path = "set_owner")
    public String setOwner(@RequestBody PutUserBody body) {
        Optional<Gruppe> groupOptional = groupService.findById(body.getId());
        if (groupOptional.isEmpty()) return  "Group not found";
        Gruppe g = groupOptional.get();
        if (body.getUser().isValidEmailAddress(body.getUserID())) {
            if (g.getOwner().getEmail().equals(body.getUser().getEmail()))
                return body.getUserFirstName() + " " + body.getUserLastName() + " is already the owner of this group";
            for (User a : g.getAdmins()) {
                if (a.getEmail().equals(body.getUserID())) {
                    User oldOwer = g.getOwner();
                    g.setOwner(body.getUser());
                    g.removeAdmin(a);
                    g.addAdmin(oldOwer);
                    groupService.save(g);
                    return "Admin " + body.getUserFirstName() + " " + body.getUserLastName() + " is now the owner of this group";
                }
            }
            for (User u : g.getUsers()) {
                if (u.getEmail().equals(body.getUserID())) {
                    User oldOwer = g.getOwner();
                    g.setOwner(body.getUser());
                    g.removeUser(u);
                    g.addAdmin(oldOwer);
                    groupService.save(g);
                    return "User " + body.getUserFirstName() + " " + body.getUserLastName() + " is now the owner of this group";
                }
            }
            return "There is no user with the email address " + body.getUserID() + " in this group";
        }
        return "Could not set owner, something is wrong with your email address (non-empty, may only contain letters, numbers, dots, periods or dashes) or your name (non-empty, may only contain letters, dashes and spaces)";
    }
}
