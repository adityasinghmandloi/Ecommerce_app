package com.task.ecommerce_api.entity;

import jakarta.persistence.*;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderItemSeqId;  // Primary Key

    @ManyToOne(optional = false)  // Foreign key to OrderHeader
    @JoinColumn(name = "order_id", nullable = false)
    private OrderHeader orderHeader;

    @ManyToOne(optional = false)  // Foreign key to Product
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Integer quantity;  // Quantity of the product
    private String status;     // Status of the order item (e.g., "Pending", "Shipped")

    // Default Constructor
    public OrderItem() {
    }

    // Getters and Setters
    public Integer getOrderItemSeqId() {
        return orderItemSeqId;
    }

    public void setOrderItemSeqId(Integer orderItemSeqId) {
        this.orderItemSeqId = orderItemSeqId;
    }

    public OrderHeader getOrderHeader() {
        return orderHeader;
    }

    public void setOrderHeader(OrderHeader orderHeader) {
        this.orderHeader = orderHeader;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
