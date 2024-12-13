package com.task.ecommerce_api.controller;

import com.task.ecommerce_api.dto.OrderRequest;


import com.task.ecommerce_api.entity.Customer;
import com.task.ecommerce_api.entity.ContactMech;
import com.task.ecommerce_api.entity.OrderHeader;
import com.task.ecommerce_api.entity.OrderItem;
import com.task.ecommerce_api.entity.Product;
import com.task.ecommerce_api.repository.CustomerRepository;
import com.task.ecommerce_api.repository.ContactMechRepository;
import com.task.ecommerce_api.repository.OrderHeaderRepository;
import com.task.ecommerce_api.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ContactMechRepository contactMechRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderHeaderRepository orderHeaderRepository;

    // Create Order
    @PostMapping
    public ResponseEntity<OrderHeader> createOrder(@RequestBody OrderRequest orderRequest) {
        // Fetch related entities (Customer, Shipping/Billing Contact Mechanism)
        Optional<Customer> customer = customerRepository.findById(orderRequest.getCustomerId());
        if (customer.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Customer not found

        Optional<ContactMech> shippingContact = contactMechRepository.findById(orderRequest.getShippingContactMechId());
        Optional<ContactMech> billingContact = contactMechRepository.findById(orderRequest.getBillingContactMechId());
        if (shippingContact.isEmpty() || billingContact.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Invalid contact mechanisms
        }

        // Create OrderHeader
        OrderHeader orderHeader = new OrderHeader();
        orderHeader.setOrderDate(orderRequest.getOrderDate());
        orderHeader.setCustomer(customer.get());
        orderHeader.setShippingContactMech(shippingContact.get());
        orderHeader.setBillingContactMech(billingContact.get());

        // Create and associate OrderItems
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderRequest.OrderItemRequest itemRequest : orderRequest.getOrderItems()) {
            Optional<Product> product = productRepository.findById(itemRequest.getProductId());
            if (product.isEmpty()) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Invalid product

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product.get());
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setStatus(itemRequest.getStatus());
            orderItem.setOrderHeader(orderHeader);  // Set the relationship between OrderItem and OrderHeader

            orderItems.add(orderItem);
        }

        orderHeader.setOrderItems(orderItems);

        // Save the order header (and cascade save the order items)
        OrderHeader savedOrder = orderHeaderRepository.save(orderHeader);

        return new ResponseEntity<>(savedOrder, HttpStatus.CREATED);  // Return the saved order
    }

    // Additional APIs can be implemented below, e.g., retrieve order, update order, etc.

}


    // Retrieve Order Details
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderHeader> getOrderDetails(@PathVariable int orderId) {
        Optional<OrderHeader> orderHeader = orderHeaderRepository.findById(orderId);
        return orderHeader.map(ResponseEntity::ok).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // Update an Order
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderHeader> updateOrder(@PathVariable int orderId, @RequestBody OrderHeader updatedOrder) {
        Optional<OrderHeader> orderHeaderOptional = orderHeaderRepository.findById(orderId);
        if (orderHeaderOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OrderHeader orderHeader = orderHeaderOptional.get();
        if (updatedOrder.getShippingContactMech() != null) {
            orderHeader.setShippingContactMech(updatedOrder.getShippingContactMech());
        }
        if (updatedOrder.getBillingContactMech() != null) {
            orderHeader.setBillingContactMech(updatedOrder.getBillingContactMech());
        }

        orderHeaderRepository.save(orderHeader);
        return new ResponseEntity<>(orderHeader, HttpStatus.OK);
    }

    // Delete an Order
    @DeleteMapping("/{orderId}")
    public ResponseEntity<Void> deleteOrder(@PathVariable int orderId) {
        Optional<OrderHeader> orderHeader = orderHeaderRepository.findById(orderId);
        if (orderHeader.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        orderHeaderRepository.delete(orderHeader.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Add an Order Item
    @PostMapping("/{orderId}/items")
    public ResponseEntity<OrderItem> addOrderItem(@PathVariable int orderId, @RequestBody OrderItem orderItem) {
        Optional<OrderHeader> orderHeader = orderHeaderRepository.findById(orderId);
        if (orderHeader.isEmpty() || productRepository.findById(orderItem.getProduct().getProductId()).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        orderItem.setOrderHeader(orderHeader.get());
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return new ResponseEntity<>(savedOrderItem, HttpStatus.CREATED);
    }

    // Update an Order Item
    @PutMapping("/{orderId}/items/{orderItemSeqId}")
    public ResponseEntity<OrderItem> updateOrderItem(@PathVariable int orderId, @PathVariable int orderItemSeqId, @RequestBody OrderItem updatedItem) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemSeqId);
        if (orderItem.isEmpty() || orderItem.get().getOrderHeader().getOrderId() != orderId) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        OrderItem existingItem = orderItem.get();
        existingItem.setQuantity(updatedItem.getQuantity());
        existingItem.setStatus(updatedItem.getStatus());
        orderItemRepository.save(existingItem);

        return new ResponseEntity<>(existingItem, HttpStatus.OK);
    }

    // Delete an Order Item
    @DeleteMapping("/{orderId}/items/{orderItemSeqId}")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable int orderId, @PathVariable int orderItemSeqId) {
        Optional<OrderItem> orderItem = orderItemRepository.findById(orderItemSeqId);
        if (orderItem.isEmpty() || orderItem.get().getOrderHeader().getOrderId() != orderId) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        orderItemRepository.delete(orderItem.get());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
