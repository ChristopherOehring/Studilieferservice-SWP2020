package com.studilieferservice.ProductManager.product;

import org.springframework.context.ApplicationEvent;

public class ProductEvent extends ApplicationEvent {
        private final Product product;

        public ProductEvent( Product product,Object source) {
            super(source);
            this.product = product;
        }
        public Product getUser(){ return product; }

}
