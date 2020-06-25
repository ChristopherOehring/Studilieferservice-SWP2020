package com.studilieferservice.shoppinglistmanager.kafka.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studilieferservice.shoppinglistmanager.item.Item;
import com.studilieferservice.shoppinglistmanager.item.ItemService;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@EnableKafka
public class ItemConsumer {

    private final ItemService itemService;

    private final ObjectMapper objectMapper;

    public ItemConsumer(ItemService itemService, ObjectMapper objectMapper) {
        this.itemService = itemService;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "productTopic", id = "product_1")
    public void consume(ConsumerRecord<String, String> consumerRecord, Acknowledgment acknowledgment){

        String message = consumerRecord.value();
        if (StringUtils.isEmpty(message)) {
            acknowledgment.acknowledge();
            return;
        }

        ItemKafkaMessage itemMessage;

        try {
            itemMessage = objectMapper.readValue(message, ItemKafkaMessage.class);
        } catch (Exception e) {
            acknowledgment.acknowledge();
            return;
        }

        if (itemMessage == null) {
            acknowledgment.acknowledge();
            return;
        }

        Item item = itemMessage.getPayload();
        itemService.createItem(item);
        acknowledgment.acknowledge();
    }
}
