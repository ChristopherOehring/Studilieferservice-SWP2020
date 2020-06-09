/*package com.studilieferservice.groupmanager.kafaka.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.service.GroupService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

//Der ganze consumer ist hier falsch, da alle neuen Gruppen hier drinne erstellt werden

@Slf4j
@Component
public class GroupConsumer {
    private final GroupService groupService;

    private final ObjectMapper objectMapper;

    public GroupConsumer(GroupService groupService, ObjectMapper objectMapper){
        this.groupService = groupService;
        this.objectMapper = objectMapper;
    }


    @KafkaListener(topics = "groups", id = "group_3")
    public void consume(ConsumerRecord<String,String> consumerRecord, Acknowledgment acknowledgment) {

        String message = consumerRecord.value();
        if (StringUtils.isEmpty(message)) {
            //log.info("Received empty message for key {}", consumerRecord.key());
            acknowledgment.acknowledge();
            return;
        }

        GroupKafkaMessage groupMessage = null;

        try {
            groupMessage = objectMapper.readValue(message, GroupKafkaMessage.class);
        } catch (Exception e) {
            //log.error("Failed to deserialize message with key {}", consumerRecord.key(), e);
            acknowledgment.acknowledge();
            return;
        }

        if (groupMessage == null) {
            //log.error("Group message is empty for key {}", consumerRecord.key());
            acknowledgment.acknowledge();
            return;
        }

        Gruppe payload = groupMessage.getPayload();
        groupService.save(payload);
        acknowledgment.acknowledge();
    }


}
*/