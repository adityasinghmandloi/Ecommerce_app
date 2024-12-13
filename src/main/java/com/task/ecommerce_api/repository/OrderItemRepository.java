package com.task.ecommerce_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.task.ecommerce_api.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Integer> {
}
