package com.studilieferservice.usermanager.kafka.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studilieferservice.usermanager.userService.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
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
public class UserProducer {
    private final KafkaTemplate<String ,String> kafkaTemplete;
    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;

    public UserProducer(KafkaTemplate<String, String> kafkaTemplete,
                        ConversionService conversionService,
                        ObjectMapper objectMapper) {
        this.kafkaTemplete = kafkaTemplete;
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
    }

    /**
     * this method is used to produce an kafka message for the given userEvent
     * and send it to the kafka topic
     *
     * @param userEvent
     */
    @EventListener
    public void produceUserEvent(UserEvent userEvent){
        UserPayload userPayload = conversionService.convert(userEvent.getUser(), UserPayload.class);
        String message = createKafkaMessage(userPayload);

        if (StringUtils.isEmpty(message)){
            return;
        }

        assert userPayload != null; // throws an error if the payload is somehow null
        kafkaTemplete.send("userTopic",userPayload.getEmail(),message);

    }

    /**
     * this method is used by produceUserEvent(..) method
     * to create an kafka message with the given userPayload
     *
     * @param userPayload
     *
     * @return kafka message as String
     */
    private String createKafkaMessage(UserPayload userPayload) {
        UserKafkaMessage message = new UserKafkaMessage(
                UUID.randomUUID().toString(),
                userPayload.getEmail(),
                ZonedDateTime.now(ZoneOffset.UTC).withNano(0).format(DateTimeFormatter.ISO_DATE_TIME),
                "new-user",
                1L,
                userPayload);

        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            return null;
        }



    }
}
