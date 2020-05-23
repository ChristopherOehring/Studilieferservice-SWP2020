package com.studilieferservice.groupmanager.kafaka.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.groupmanager.kafaka.KafkaMessage;
import com.studilieferservice.groupmanager.persistence.Gruppe;

public class GroupDeletionKafkaMessage extends KafkaMessage<Gruppe> {
    public GroupDeletionKafkaMessage(@JsonProperty("id") String id,
                                     @JsonProperty("key")String key,
                                     @JsonProperty("time") String time,
                                     @JsonProperty("type") String type,
                                     @JsonProperty("version") Long version,
                                     @JsonProperty("payload") Gruppe payload) {
        super(id, key, time, type, version, payload);
    }

}
