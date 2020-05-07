package com.manu.prototype.group;

import com.manu.prototype.group.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface JpaGroupRepository extends JpaRepository<Group, UUID> {

}
