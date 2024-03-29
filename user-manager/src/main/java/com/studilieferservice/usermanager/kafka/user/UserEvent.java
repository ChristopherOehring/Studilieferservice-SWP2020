package com.studilieferservice.usermanager.kafka.user;

import com.studilieferservice.usermanager.user.User;
import org.springframework.context.ApplicationEvent;

/**
 * @author Seraj Hadros
 * @version 1.1 6/23/20
 */
public class UserEvent extends ApplicationEvent {
    private final User user;
    private final UserEventType type;

    public UserEvent(User user, Object source, UserEventType type) {
        super(source);
        this.user = user;
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public UserEventType getType() {
        return type;
    }
}
