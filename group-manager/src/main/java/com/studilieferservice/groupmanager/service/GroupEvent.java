package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import org.springframework.context.ApplicationEvent;

/**
 * This Event is created everyTime
 */
public class GroupEvent extends ApplicationEvent {

    private final Gruppe group;
    private final GroupEventType type;

    public GroupEvent(Gruppe group, Object source, GroupEventType type){
        super(source);
        this.group = group;
        this.type = type;
    }

    public Gruppe getGroup() {
        return group;
    }

    public GroupEventType getType() {
        return type;
    }
}
