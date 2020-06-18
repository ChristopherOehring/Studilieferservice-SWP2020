package com.studilieferservice.ProductManager.product;

import org.attoparser.dom.Text;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Product Model with the respective attributes and their setters and getters
 *
 */

@Entity
public class Product {

     @Id
     private String name;

     private String description;

     @NotNull
     private double price;

     //kafka should then use imageUrl.getContent() in payload
     private Text imageUrl;

     public Product() { }

     //minimal constructor
     public Product(String name, double price) {
          this.name = name;
          this.price = price;
     }

     public Product(String name, String description, double price, Text imageUrl) {
          this.name = name;
          this.description = description;
          this.price = price;
          this.imageUrl = imageUrl;
     }

     public String getName() {
          return name;
     }

/*     public void setName(String name) {
          this.name = name;
     }*/

     public String getDescription() {
          return description;
     }

     public void setDescription(String description) {
          this.description = description;
     }

     public double getPrice() {
          return price;
     }

     public void setPrice(double price) {
          this.price = price;
     }

     public String getImageUrl() {
          return imageUrl.getContent();
     }

     public void setImageUrl(String imageUrl) {
          this.imageUrl.setContent(imageUrl);
     }

     @Override
     public String toString() {
          return "Product [" +
                  "name='" + name + '\'' +
                  ", price=" + price +
                  ", description='" + description + '\'' +
                  ", imageUrl=" + imageUrl.getContent() +
                  ']';
     }
}
