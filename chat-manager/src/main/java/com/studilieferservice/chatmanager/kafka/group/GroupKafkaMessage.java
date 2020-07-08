package com.studilieferservice.chatmanager.kafka.group;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.chatmanager.kafka.KafkaMessage;

public class GroupKafkaMessage extends KafkaMessage<GroupKafka> {

    public GroupKafkaMessage(@JsonProperty("id") String id,
                             @JsonProperty("key")String key,
                             @JsonProperty("time") String time,
                             @JsonProperty("type") String type,
                             @JsonProperty("version") Long version,
                             @JsonProperty("payload") GroupKafka payload) {
        super(id, key, time, type, version, payload);
    }
}

/*
Group payload:
    String id;
    String groupName;
    String owner;
    List<String> userList;
    List<String> adminList;
*/
