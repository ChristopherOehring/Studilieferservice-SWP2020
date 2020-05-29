package com.studilieferservice.groupmanager.kafka.group;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class GroupToPayloadConverter implements Converter<Gruppe, GroupPayload> {
    @Override
    public GroupPayload convert(Gruppe gruppe) {
        return new GroupPayload(
                gruppe.getId(),
                gruppe.getGroupName(),
                gruppe.getOwner().getEmail(),
                gruppe.getAdmins().stream().map(User::getEmail).collect(Collectors.toList()),
                gruppe.getUsers().stream().map(User::getEmail).collect(Collectors.toList()),
                gruppe.getVersion()
        );
    }
}