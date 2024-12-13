package com.task.ecommerce_api.entity;

import jakarta.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

@Entity
public class OrderHeader {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer orderId;  // Primary Key

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;  // Order Date

    @ManyToOne(optional = false)  // Non-nullable foreign key to Customer
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(optional = false)  // Non-nullable foreign key to Shipping ContactMech
    @JoinColumn(name = "shipping_contact_mech_id", nullable = false)
    private ContactMech shippingContactMech;

    @ManyToOne(optional = false)  // Non-nullable foreign key to Billing ContactMech
    @JoinColumn(name = "billing_contact_mech_id", nullable = false)
    private ContactMech billingContactMech;

    @OneToMany(mappedBy = "orderHeader", cascade = CascadeType.ALL, orphanRemoval = true)  // Cascade operations and ensure integrity
    private List<OrderItem> orderItems = new ArrayList<>();

    // Default Constructor
    public OrderHeader() {
    }

    // Getters and Setters
    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public ContactMech getShippingContactMech() {
        return shippingContactMech;
    }

    public void setShippingContactMech(ContactMech shippingContactMech) {
        this.shippingContactMech = shippingContactMech;
    }

    public ContactMech getBillingContactMech() {
        return billingContactMech;
    }

    public void setBillingContactMech(ContactMech billingContactMech) {
        this.billingContactMech = billingContactMech;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public void addOrderItem(OrderItem item) {
        item.setOrderHeader(this);  // Ensure the relationship is set
        this.orderItems.add(item);
    }

    public void removeOrderItem(OrderItem item) {
        item.setOrderHeader(null);  // Break the relationship
        this.orderItems.remove(item);
    }
}

