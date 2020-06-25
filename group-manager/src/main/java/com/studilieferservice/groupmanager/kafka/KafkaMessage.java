package com.studilieferservice.groupmanager.kafka;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A template for Kafka Messages
 * @param <P> The Payload Class
 * @author Christopher Oehring
 * @version 1.1 6/18/20
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class KafkaMessage<P> {

    private final String id;

    private final String key;

    private final String time;

    private final String type;

    private final Long version;

    private final P payload;

    public KafkaMessage(@JsonProperty("id") String id,
                        @JsonProperty("key")String key,
                        @JsonProperty("time") String time,
                        @JsonProperty("type") String type,
                        @JsonProperty("version") Long version,
                        @JsonProperty("payload") P payload) {
        this.id = id;
        this.key = key;
        this.time = time;
        this.type = type;
        this.version = version;
        this.payload = payload;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public String getTime() {
        return time;
    }

    public String getType() {
        return type;
    }

    public Long getVersion() {
        return version;
    }

    public P getPayload() {
        return payload;
    }
}
