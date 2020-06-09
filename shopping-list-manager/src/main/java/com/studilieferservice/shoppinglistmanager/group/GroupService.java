package com.studilieferservice.shoppinglistmanager.group;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GroupService {

    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Group createGroup(Group group) {
        return groupRepository.save(group);
    }

    public Group getGroup(String id) {
        return groupRepository.findById(id).orElseThrow();
    }

    public void deleteGroup(Group group) {
        groupRepository.delete(group);
    }
}
