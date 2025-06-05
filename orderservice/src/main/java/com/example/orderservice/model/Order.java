package com.example.orderservice.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;
    private Long storeId;
    private Long productId;

    private String size;
    private String color;
    private int quantity;
    private double unitPrice;
    private LocalDateTime orderDate;

    public Order() {}

    public Order(Long userId, Long storeId, Long productId, String size, String color, int quantity, double unitPrice, LocalDateTime orderDate) {
        this.userId = userId;
        this.storeId = storeId;
        this.productId = productId;
        this.size = size;
        this.color = color;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.orderDate = orderDate;
    }

    // Getteri/Setteri
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public Long getProductId() { return productId; }
    public void setProductId(Long productId) { this.productId = productId; }

    public String getSize() { return size; }
    public void setSize(String size) { this.size = size; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public LocalDateTime getOrderDate() { return orderDate; }
    public void setOrderDate(LocalDateTime orderDate) { this.orderDate = orderDate; }
}
