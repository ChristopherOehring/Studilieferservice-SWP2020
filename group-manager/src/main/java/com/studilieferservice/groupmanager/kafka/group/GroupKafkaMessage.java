package com.studilieferservice.groupmanager.kafka.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.groupmanager.kafka.KafkaMessage;

/**
 * This contains the basic elements of a {@link KafkaMessage} and a {@link GroupPayload}
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */
public class GroupKafkaMessage extends KafkaMessage<GroupPayload> {
    public GroupKafkaMessage(@JsonProperty("id") String id,
                             @JsonProperty("key")String key,
                             @JsonProperty("time") String time,
                             @JsonProperty("type") String type,
                             @JsonProperty("version") Long version,
                             @JsonProperty("payload") GroupPayload payload) {
        super(id, key, time, type, version, payload);
    }

}
