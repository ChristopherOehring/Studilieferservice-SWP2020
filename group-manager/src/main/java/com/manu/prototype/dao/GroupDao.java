package com.manu.prototype.dao;

import com.manu.prototype.group.Group;
import java.util.List;
import java.util.UUID;

public interface GroupDao {

    /**
     * Basisschnittstelle zur Gruppenerzeugung
     * //@param user2 zweiter Nutzer - für eine Gruppe sind zwei Nutzer notwendig
     * @return neue Gruppe
     */
    default Group createGroup(String name) {
        UUID randGroupID = UUID.randomUUID();
        return createGroup(randGroupID, name);
    }
    Group createGroup(UUID GroupID, String name);
    int deleteGroup(UUID id);
    int deleteGroup(Group g);

    int addUserToGroup(String user, UUID id);
    int removeUserFromGroup(String user, UUID id);

    List<Group> getAllGroupsOfUser(String user);
    List<String> getAllUsersOfGroup(Group group);
    List<Group> getAllGroups();

    Group getGroup(UUID id);

    /*Group getGroupById(UUID id);
    int deleteGroupByID(UUID groupId);
    String getGroupNameByID(UUID groupId);
    */

    //int addUserToGroupByID(UUID id, Group group);
    //Optional<String> getUserByID(UUID id, Group group);
    //int removeUserFromGroupByID(UUID UserId, Group group);



}
