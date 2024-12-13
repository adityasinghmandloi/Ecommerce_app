package com.task.ecommerce_api.repository;


import com.task.ecommerce_api.entity.OrderHeader;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHeaderRepository extends JpaRepository<OrderHeader, Integer> {}
