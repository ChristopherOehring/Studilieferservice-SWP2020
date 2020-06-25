package com.studilieferservice.usermanager.kafka.user;

import com.studilieferservice.usermanager.user.User;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;

/**
 * to convert the User object to UserPayload object
 *
 * @author Seraj Hadros
 * @version 1.1 6/23/20
 */
@Component
public class UserToPayloadConverter implements Converter<User, UserPayload> {
    @Override
    public UserPayload convert(User user) {
        return new UserPayload(
                user.getFirstName(),
                user.getLastName(),
                user.getUserName(),
                user.getStreet(),
                user.getCity(),
                user.getZip(),
                user.getEmail()
        );
    }

}
