package com.studilieferservice.groupmanager.kafka.group;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.studilieferservice.groupmanager.service.GroupEvent;
import com.studilieferservice.groupmanager.service.GroupEventType;

import org.springframework.context.event.EventListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * This Component creates kafka messages on the topic: "groupTopic" <br>
 * These Messages are based on GroupEvents<br>
 * Every Message contains a {@link GroupKafkaMessage} which, among other things, contains a {@link GroupPayload}<br>
 * <br>
 * The resulting JSON looks a little like this:<br>
 * <pre>
 {
     "id":"3ec50b55-f6e6-4b0d-b821-b8751d282aa3",
     "key":"65c57e94-e417-4578-85d5-cb2bbd7085fd",
     "time":"2020-06-09T03:57:21Z",
     "type":"UPDATE",
     "version":3,
     "payload":{
         "id":"65c57e94-e417-4578-85d5-cb2bbd7085fd",
         "groupName":"test",
         "owner":"max.mustermann@tu-ilmenau.de",
         "userList":[],
         "adminList":[]
     }
 }
 * </pre>
 * The values contain the following information:<br>
 * <pre>
     id: A UUID that identifies this message
     key: The UUID that identifies the Group
     time: the time at which this message was sent
     type: The type of operation this message indicates (namely: UPDATE or DELETION
     version: the version of the group that can be found in this message
     payload: A representation of the relevant values of the group
 * </pre>

 */

@Component
public class GroupProducer {

    private final KafkaTemplate<String,String> kafkaTemplate;

    private final ConversionService conversionService;

    private final ObjectMapper objectMapper;

    public GroupProducer(KafkaTemplate<String, String> kafkaTemplate,
                         ConversionService conversionService,
                         ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
    }

    /**
     * Sendet eine KafkaMessage, wenn eine Gruppe erstellt wird.<br>
     * Diese enthält die unter anderem die Gruppe.
     * @param event wird generiert, wenn eine gruppe erstellt wird
     */
    @EventListener
    public void produceGroupEvent(GroupEvent event){
        GroupPayload payload = conversionService.convert(event.getGroup(), GroupPayload.class);
        String message = createKafkaMessage(event.getGroup().getVersion(), payload, event.getType());
        if(StringUtils.isEmpty(message)){
            System.out.println("Cannot send empty message");
            return;
        }

        assert payload != null; // throws an error if the payload is somehow null
        kafkaTemplate.send("groupTopic", payload.getId(), message);
    }


    private String createKafkaMessage(Long version, GroupPayload payload, GroupEventType type) {

        GroupKafkaMessage message = new GroupKafkaMessage(
                UUID.randomUUID().toString(),
                payload.getId(),
                ZonedDateTime.now(ZoneOffset.UTC).withNano(0).format(DateTimeFormatter.ISO_DATE_TIME),
                type.name(),
                version,
                payload
        );

        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            System.out.println("Failed to serialize group message");
            return null;
        }
    }
}
