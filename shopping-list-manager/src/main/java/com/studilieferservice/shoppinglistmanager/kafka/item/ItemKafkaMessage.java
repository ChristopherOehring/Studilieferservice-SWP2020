package com.studilieferservice.shoppinglistmanager.kafka.item;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.kafka.KafkaMessage;

public class ItemKafkaMessage extends KafkaMessage<Item> {

    public ItemKafkaMessage(@JsonProperty("id") String id,
                            @JsonProperty("key") String key,
                            @JsonProperty("time") String time,
                            @JsonProperty("type") String type,
                            @JsonProperty("version") Long version,
                            @JsonProperty("payload") Item payload) {
        super(id, key, time, type, version, payload);
    }
}

/*
Item payload:
    String name;
    String description;
    double price;
    String imageUrl;
*/
