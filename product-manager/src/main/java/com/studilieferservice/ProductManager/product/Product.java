package com.studilieferservice.ProductManager.product;

import org.attoparser.dom.Text;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.persistence.*;
/**
 * Product Model with the respective attributes and their setters and getters
 *
 */

@Entity
@Table(name = "product")
public class Product {

     @Id
     @Column(name ="name")
     @NotNull
     private String name;

     @Column(name ="description")
     private String description;

     @NotNull
     @Column(name ="price")
     private double price;

     //kafka should then use imageUrl.getContent() in payload
     @Column(name ="imageUrl")
     private Text imageUrl;

     public Product() { }

     //minimal constructor
     public Product(String name, double price) {
          this.name = name;
          this.price = price;
          description = "";
          imageUrl = new Text("");
     }

     public Product(String name, String description, double price, String imageUrl) {
          this.name = name;
          this.description = description;
          this.price = price;
          this.imageUrl = new Text(imageUrl);
     }

     public String getName() {
          return name;
     }

     public void setName(String name) {
          this.name = name;
     }

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

     public Text getImageUrl() {
          return imageUrl;
     }

     public void setImageUrl(String imageUrl) {
          this.imageUrl = new Text(imageUrl);
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
