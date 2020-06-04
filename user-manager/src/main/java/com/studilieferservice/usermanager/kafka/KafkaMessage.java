package com.studilieferservice.usermanager.kafka;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * basic structure of the kafka message
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class KafkaMessage<U> {

    private final String id;
    private final String key;
    private final String time;
    private final String type;
    private final Long version;
    private final U payload;


    protected KafkaMessage(@JsonProperty("id") String id,
                           @JsonProperty("key") String key,
                           @JsonProperty("time") String time,
                           @JsonProperty("type") String type,
                           @JsonProperty("version") Long version,
                           @JsonProperty("payload") U payload) {
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

    public U getPayload() {
        return payload;
    }
}
