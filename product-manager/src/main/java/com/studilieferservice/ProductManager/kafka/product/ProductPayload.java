package com.studilieferservice.ProductManager.kafka.product;
import org.attoparser.dom.Text;

public class ProductPayload {
    private String name;
    private String description;
    private double price;
    private Text imageUrl;

    public ProductPayload(String name, String description, double price, Text imageUrl) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public Text getImageUrl() {
        return imageUrl;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setImageUrl(Text imageUrl) {
        this.imageUrl = imageUrl;
    }
}
