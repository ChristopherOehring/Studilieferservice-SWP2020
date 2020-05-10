package com.manu.prototype.service;

import com.manu.prototype.persistence.*;
import org.apache.kafka.common.protocol.types.Field;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
