package com.manu.prototype.service;

import com.manu.prototype.dao.GroupDao;
import com.manu.prototype.group.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupDao groupDao;

    @Autowired
    public GroupService(@Qualifier("testGroupDao") GroupDao groupDao) {
        this.groupDao = groupDao;
    }

    public UUID createGroup(String name) {
        UUID id = groupDao.createGroup(name).getGroupId();
        addUser("test@testmail.de", id);
        return id;
    }

    public int addUser(String user, UUID id) {
        return groupDao.addUserToGroup(user, id);
    }

    public List<String> getAllUsersOfGroup(Group group) {
        return groupDao.getAllUsersOfGroup(group);
    }

    public List<Group> getAllGroupsOfUser(String user) {
        return groupDao. getAllGroupsOfUser(user);
    }

    public List<Group> getAllGroups(){
        return groupDao.getAllGroups();
    }

    public int removeUserFromGroup(String user, UUID id) {
        return groupDao.removeUserFromGroup(user, id);
    }

    public int deleteGroup(UUID id) {
        return groupDao.deleteGroup(id);
    }

}
