package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.JpaGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * just a normal service for handling JpaRepositories
 * save: saves Groups to the repository
 * findGroup: give it a group ID (String) and you get the group with group ID, group name and all users (if this group exists)
 * findAll: gets you all groups with group ID, group name and all users
 * findById: no difference to findGroup - maybe we will remove one of these methods later on
 * deleteById: removes a group from the repository when given the group ID of the specific group
 */
@Service
public class GroupService {

    private final JpaGroupRepository groupRepository;

    @Autowired
    public GroupService(JpaGroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    public Gruppe save(Gruppe group) {
        return groupRepository.save(group);
    }

    public Optional<Gruppe> findGroup(String id){
        return  groupRepository.findById(id);
    }

    public List<Gruppe> findAll() {
        return groupRepository.findAll();
    }

    public Optional<Gruppe> findById(String id) {
        return groupRepository.findById(id);
    }

    public void deleteById(String id) {
        groupRepository.deleteById(id);
    }
}
