package com.manu.prototype.dao;

import com.manu.prototype.group.Group;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Repository("testGroupDao")
public class TestGroupDataAccessService implements GroupDao {

    private List<Group> allGroups = new ArrayList<>();

    @Override
    public Group createGroup(UUID randID, String name) {
        Group group = new Group(randID, name);
        allGroups.add(group);
        return group;
    }

    @Override
    public int deleteGroup(UUID id) {
        return deleteGroup(getGroup(id));
    }

    @Override
    public int deleteGroup(Group group) {
        System.out.println(group);
        System.out.println(allGroups.remove(group));
        System.out.println(allGroups.toString());
        return 0;
    }

    @Override
    public int addUserToGroup(String user, UUID id) {
        Group group = getGroup(id);
        group.addUser(user);
        return 1;
    }

    @Override
    public int removeUserFromGroup(String user, UUID id) {
        Group group = getGroup(id);
        if (group.getUsers().contains(user)) {
            group.removeUser(user);
        }
        return 1;
    }

    @Override
    public List<Group> getAllGroupsOfUser(String user){
        List<Group> ret = new ArrayList<>();
        for (Group g : allGroups) {
            if(g.getUsers().contains(user)) ret.add(g);
        }
        return ret;
    }

    @Override
    public List<String> getAllUsersOfGroup(Group group){
        return group.getUsers();
    }

    @Override
    public List<Group> getAllGroups() {
        return allGroups;
    }

    @Override
    public Group getGroup(UUID id){
        for (Group g: allGroups) {
            if(g.getGroupId().equals(id)){
                return g;
            }
        }
        return null;
    }

//-------------------------------------not jet implemented:-----------------------------------------------------------
    /*



    @Override
    public int deleteGroupByID(UUID id) {
        return 0;
    }

    @Override
    public String getGroupNameByID(UUID id) {
        return null;
    }

    @Override
    public int removeUserFromGroupByID(UUID id, Group group) {
        Optional<User> tmp = GroupDatabase.stream().filter(user -> user.getUserId().equals(id)).findFirst();
        GroupDatabase.remove(tmp);
        group.removeOneFromUserCount();
        if (getUserCount(group)==0) {
           deleteGroup(group);
        }
        return 1;
    }

    public List<Group> getAllGroups() {
        return AllGroups;
    }


    @Override
    public int addUserToGroupByID(UUID id, Group group) {
        GroupDatabase.stream().filter(user -> user.getUserId().equals(id)).findFirst();
        return 0;
    }

    @Override
    public Optional<User> getUserByID(UUID id, Group group) {
        return GroupDatabase.stream().filter(user -> user.getUserId().equals(id)).findFirst();
    }

    */
}
