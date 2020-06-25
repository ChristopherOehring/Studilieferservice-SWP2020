package com.studilieferservice.ProductManager.kafka.product;

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

@Component
public class ProductProducer {
    private final KafkaTemplate<String,String> kafkaTemplate;
    private final ConversionService conversionService;
    private final ObjectMapper objectMapper;

    public ProductProducer(KafkaTemplate<String, String> kafkaTemplate,
                           ConversionService conversionService,
                           ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.conversionService = conversionService;
        this.objectMapper = objectMapper;
    }

    @EventListener
    public void produceProductEvent(ProductEvent productEvent){
        ProductPayload productPayload = conversionService
                .convert(productEvent.getProduct(), ProductPayload.class);
        String message = createKafkaMessage(productPayload);

        if (StringUtils.isEmpty(message)){
            System.out.println("Cannot send empty message");
            return;
        }

        assert productPayload != null;
        kafkaTemplate.send("productTopic", productPayload.getName(), message);
    }

    private String createKafkaMessage(ProductPayload productPayload) {
        ProductKafkaMessage message = new ProductKafkaMessage(
                UUID.randomUUID().toString(),
                productPayload.getName(),
                ZonedDateTime.now(ZoneOffset.UTC).withNano(0).format(DateTimeFormatter.ISO_DATE_TIME),
                "new-product",
                1L,
                productPayload
        );

        try {
            return objectMapper.writeValueAsString(message);
        } catch (Exception e) {
            System.out.println("Failed to serialize group message");
            return null;
        }
    }
}
