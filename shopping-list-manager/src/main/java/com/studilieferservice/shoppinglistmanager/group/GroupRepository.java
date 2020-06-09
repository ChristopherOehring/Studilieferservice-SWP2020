package com.studilieferservice.shoppinglistmanager.group;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, String> {

    //Group findById(String id);
}

