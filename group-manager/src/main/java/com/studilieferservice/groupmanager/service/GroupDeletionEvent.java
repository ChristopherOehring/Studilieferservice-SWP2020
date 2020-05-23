package com.studilieferservice.groupmanager.service;

import org.springframework.context.ApplicationEvent;

public class GroupDeletionEvent extends ApplicationEvent {

    private final String id;

    public GroupDeletionEvent(Object source, String id) {
        super(source);
        this.id = id;
    }

    public String getId() {
        return id;
    }

}
