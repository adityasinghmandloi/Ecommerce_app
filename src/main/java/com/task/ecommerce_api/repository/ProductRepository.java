package com.task.ecommerce_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.ecommerce_api.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {}
