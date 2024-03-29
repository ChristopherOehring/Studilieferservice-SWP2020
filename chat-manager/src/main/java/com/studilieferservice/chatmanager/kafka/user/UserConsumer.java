package com.studilieferservice.chatmanager.kafka.user;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.studilieferservice.chatmanager.group.GroupService;
import com.studilieferservice.chatmanager.user.User;
import com.studilieferservice.chatmanager.user.UserService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@EnableKafka
public class UserConsumer {

    private final UserService userService;
    private final GroupService groupService;

    private final ObjectMapper objectMapper;

    public UserConsumer(UserService userService, GroupService groupService, ObjectMapper objectMapper) {
        this.userService = userService;
        this.groupService = groupService;
        this.objectMapper = objectMapper;
    }

    @Transactional
    @KafkaListener(topics = "userTopic", id = "user_3")
    public void consume(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment){

        String message = consumerRecord.value();
        if (StringUtils.isEmpty(message)) {
            acknowledgment.acknowledge();
            return;
        }

        UserKafkaMessage userMessage;

        try {
            userMessage = objectMapper.readValue(message, UserKafkaMessage.class);
        } catch (Exception e) {
            acknowledgment.acknowledge();
            return;
        }

        if (userMessage == null) {
            acknowledgment.acknowledge();
            return;
        }

        User payload = userMessage.getPayload();

        if (userMessage.getType().equals("UPDATE")) {
            String oldName = userService.getUser(payload.getId()).getName();
            User u = userService.updateUserFromKafka(payload.getId(), payload.getName());
            groupService.addUpdateMessageToUser(u, oldName);
        } else {
            userService.createUserFromKafka(payload.getId(), payload.getName());
        }
        acknowledgment.acknowledge();
    }
}
