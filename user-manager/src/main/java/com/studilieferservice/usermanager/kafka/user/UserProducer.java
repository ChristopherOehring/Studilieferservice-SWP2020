package com.studilieferservice.usermanager.kafka.user;

import com.fasterxml.jackson.databind.ObjectMapper;

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
 * This Component creates kafka messages on the topic: "userTopic" <br>
 * These Messages are based on UserEvents<br>
 * Every Message contains a {@link UserKafkaMessage} which, among other things, contains a {@link UserPayload}<br>
 * <br>
 * The resulting JSON looks a little like this:<br>
 * <pre>
 {
 "id":"60febc8d-5a5f-4132-b678-20ff37c75305",
 "key":"userMann@web.com",
 "time":"2020-06-24T17:08:23Z",
 "type":"NEW",
 "version":1,
 "payload":{"firstName":"Muster","lastName":"muster","userName":"musterMuster","street":"Am Markt","city":"Ilmenau","zip":"98396","email":"userMann@web.com"}
 }
 * </pre>
 * The values contain the following information:<br>
 * <pre>
 id: A UUID that identifies this message
 key: The UUID that identifies the User
 time: the time at which this message was sent
 type: The type of operation this message indicates (namely: UPDATE or NEW)
 version: 1
 payload: A representation of the relevant values of the user
 * </pre>
 * @author Seraj Hadros
 * @version 1.1 6/24/20
 */

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
        String message = createKafkaMessage(userPayload, userEvent.getType());

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
    private String createKafkaMessage(UserPayload userPayload, UserEventType userEventType) {
        UserKafkaMessage message = new UserKafkaMessage(
                UUID.randomUUID().toString(),
                userPayload.getEmail(),
                ZonedDateTime.now(ZoneOffset.UTC).withNano(0).format(DateTimeFormatter.ISO_DATE_TIME),
                userEventType.name(),
                1L,
                userPayload);

        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            return null;
        }
    }
}
