package com.studilieferservice.groupmanager.kafaka.group;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studilieferservice.groupmanager.service.GroupCreationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.convert.ConversionService;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

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

    @EventListener
    public void produceGroupEvent(GroupCreationEvent creationEvent){
        
    }
}
