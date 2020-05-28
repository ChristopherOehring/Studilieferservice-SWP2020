package com.studilieferservice.groupmanager.kafaka.group;

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
     * Sendet eine KafkaMessage, wenn eine Gruppe erstellt wird
     * Diese enthält die Gruppe
     * @param event wird generiert, wenn eine gruppe erstellt wird
     */
    @EventListener
    public void produceGroupEvent(GroupEvent event){
        GroupPayload payload = conversionService.convert(event.getGroup(), GroupPayload.class);
        String message = createKafkaMessage(payload, event.getType());
        if(StringUtils.isEmpty(message)){
            System.out.println("Cannot send empty message");
            return;
        }

        assert payload != null; // throws an error if the payload is somehow null
        kafkaTemplate.send("groups", payload.getId(), message);
    }


    private String createKafkaMessage(GroupPayload payload, GroupEventType type) {

        GroupKafkaMessage message = new GroupKafkaMessage(
                UUID.randomUUID().toString(),
                payload.getId(),
                ZonedDateTime.now(ZoneOffset.UTC).withNano(0).format(DateTimeFormatter.ISO_DATE_TIME),
                type.name(),
                payload.getVersion(),
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
