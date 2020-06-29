package com.studilieferservice.chatmanager.kafka.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@EnableKafka
public class GroupConsumer {

    private final GroupKafkaService groupKafkaService;

    private final ObjectMapper objectMapper;

    public GroupConsumer(GroupKafkaService groupKafkaService, ObjectMapper objectMapper){
        this.groupKafkaService = groupKafkaService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "groupTopic", id = "group_2")
    public void consume(ConsumerRecord<String,String> consumerRecord, Acknowledgment acknowledgment) {

        String message = consumerRecord.value();
        if (StringUtils.isEmpty(message)) {
            acknowledgment.acknowledge();
            return;
        }

        GroupKafkaMessage groupMessage;

        try {
            groupMessage = objectMapper.readValue(message, GroupKafkaMessage.class);
        } catch (Exception e) {
            acknowledgment.acknowledge();
            return;
        }

        if (groupMessage == null) {
            acknowledgment.acknowledge();
            return;
        }

        GroupKafka payload = groupMessage.getPayload();

        if (groupMessage.getType().equals("DELETION")) {
            groupKafkaService.deleteGroupFromKafka(payload.getId());
        } else if (groupKafkaService.groupAlreadyExists(payload.getId())) {
            groupKafkaService.updateGroupFromKafka(payload.getId(), payload.getAllMembers());
        } else {
            groupKafkaService.createGroupFromKafka(payload.getId(), payload.getGroupName(), payload.getAllMembers());
        }
        acknowledgment.acknowledge();
    }
}
