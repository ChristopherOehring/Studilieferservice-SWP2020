package com.studilieferservice.ProductManager.kafka.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.studilieferservice.ProductManager.kafka.KafkaMessage;

public class ProductKafkaMessage extends KafkaMessage<ProductPayload> {
    protected ProductKafkaMessage(@JsonProperty("id") String id,
                                  @JsonProperty("key") String key,
                                  @JsonProperty("time") String time,
                                  @JsonProperty("type") String type,
                                  @JsonProperty("version") Long version,
                                  @JsonProperty("payload") ProductPayload payload) {
        super(id, key, time, type, version, payload);
    }

}
