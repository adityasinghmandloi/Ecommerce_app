package com.task.ecommerce_api.dto;


import java.util.Date;
import java.util.List;

public class OrderRequest {

    private Date orderDate;
    private Integer customerId; // Use Integer for null safety
    private Integer shippingContactMechId;
    private Integer billingContactMechId;
    private List<OrderItemRequest> orderItems;

    // Getters and Setters
    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getShippingContactMechId() {
        return shippingContactMechId;
    }

    public void setShippingContactMechId(Integer shippingContactMechId) {
        this.shippingContactMechId = shippingContactMechId;
    }

    public Integer getBillingContactMechId() {
        return billingContactMechId;
    }

    public void setBillingContactMechId(Integer billingContactMechId) {
        this.billingContactMechId = billingContactMechId;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemRequest> orderItems) {
        this.orderItems = orderItems;
    }

    // Nested class for Order Item Requests
    public static class OrderItemRequest {
        private Integer productId; // Use Integer for null safety
        private Integer quantity;
        private String status;

        // Getters and Setters
        public Integer getProductId() {
            return productId;
        }

        public void setProductId(Integer productId) {
            this.productId = productId;
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
}
