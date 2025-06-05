package com.example.inventoryservice.model;

import jakarta.persistence.*;

@Entity
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long storeId;
    private Long productId;

    private String size;
    private String color;
    private int stock;

    public Inventory() {}

    public Inventory(Long storeId, Long productId, String size, String color, int stock) {
        this.storeId = storeId;
        this.productId = productId;
        this.size = size;
        this.color = color;
        this.stock = stock;
    }

    // Getteri/Setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
