package com.studilieferservice.groupmanager.kafka.group;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Has to be implemented to enable the use of
 * {@link org.springframework.core.convert.ConversionService#convert(java.lang.Object, java.lang.Class)}
 * to convert objects of {@link Gruppe} to objects of {@link Gruppe}
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */

@Component
public class GroupToPayloadConverter implements Converter<Gruppe, GroupPayload> {
    @Override
    public GroupPayload convert(Gruppe gruppe) {
        return new GroupPayload(
                gruppe.getId(),
                gruppe.getGroupName(),
                gruppe.getOwner().getEmail(),
                gruppe.getAdmins().stream().map(User::getEmail).collect(Collectors.toList()),
                gruppe.getMembers().stream().map(User::getEmail).collect(Collectors.toList())
        );
    }
}