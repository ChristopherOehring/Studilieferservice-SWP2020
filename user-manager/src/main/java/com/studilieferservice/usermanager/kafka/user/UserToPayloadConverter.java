package com.studilieferservice.usermanager.kafka.user;

import com.studilieferservice.usermanager.userService.User;
import org.springframework.stereotype.Component;
import org.springframework.core.convert.converter.Converter;


@Component
public class UserToPayloadConverter implements Converter<User, UserPayload> {
    @Override
    public UserPayload convert(User user){
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
