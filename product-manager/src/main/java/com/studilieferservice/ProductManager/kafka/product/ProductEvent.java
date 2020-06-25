package com.studilieferservice.ProductManager.kafka.product;

import com.studilieferservice.ProductManager.product.Product;
import org.springframework.context.ApplicationEvent;

public class ProductEvent extends ApplicationEvent {

    private final Product product;

    public ProductEvent(Product product, Object source) {
        super(source);
        this.product = product;
    }

    public Product getProduct(){
        return product ;
    }
}
