package com.studilieferservice.groupmanager.kafka.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.groupmanager.kafka.KafkaMessage;

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
