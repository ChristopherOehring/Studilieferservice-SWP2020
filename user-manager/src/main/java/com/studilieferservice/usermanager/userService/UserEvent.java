package com.studilieferservice.usermanager.userService;


import org.springframework.context.ApplicationEvent;


public class UserEvent extends ApplicationEvent {
    private final User user;

    public UserEvent( User user,Object source) {
        super(source);
        this.user = user;
    }
    public User getUser(){ return user; }
}
