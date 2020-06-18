package com.studilieferservice.ProductManager;

import com.sun.istack.NotNull;
import org.attoparser.dom.Text;
import org.hibernate.type.descriptor.sql.LongVarcharTypeDescriptor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.sql.Blob;

@Entity
public class Product {

     @Id
     String name;
     String description;

     public Text getImageUrl() {
          return ImageUrl;
     }

     public void setImageUrl(Text imageUrl) {
          ImageUrl = imageUrl;
     }

     Text ImageUrl;
     int price;



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


     public int getPrice() {
          return price;
     }

     public void setPrice(int price) {
          this.price = price;
     }
}
