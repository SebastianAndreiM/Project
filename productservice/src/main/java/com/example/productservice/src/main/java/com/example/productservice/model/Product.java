package com.example.productservice.model;

import jakarta.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private String producer;
    private String category;    // Adăugat pentru filtrare
    private String description;
    private String imageUrl;    // Path sau URL imagine
    private double price;

    public Product() {}

    public Product(String model, String producer, String category, String description, String imageUrl, double price) {
        this.model = model;
        this.producer = producer;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    // Getteri/Setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public String getProducer() { return producer; }
    public void setProducer(String producer) { this.producer = producer; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
}
