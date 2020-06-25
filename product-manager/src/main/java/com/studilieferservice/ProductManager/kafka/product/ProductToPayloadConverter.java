package com.studilieferservice.ProductManager.kafka.product;

import com.studilieferservice.ProductManager.product.Product;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ProductToPayloadConverter implements Converter<Product, ProductPayload> {

    @Override
    public ProductPayload convert(Product product){
        return new ProductPayload(
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getImageUrl()
        );
    }
}
