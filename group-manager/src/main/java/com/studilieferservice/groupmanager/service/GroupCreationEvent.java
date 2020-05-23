package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import org.springframework.context.ApplicationEvent;

public class GroupCreationEvent extends ApplicationEvent {

    private final Gruppe group;

    public GroupCreationEvent(Gruppe group, Object scource){
        super(scource);
        this.group = group;
    }

    public Gruppe getGroup() {
        return group;
    }
}
