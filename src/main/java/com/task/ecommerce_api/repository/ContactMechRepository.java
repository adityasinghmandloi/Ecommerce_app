package com.task.ecommerce_api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.task.ecommerce_api.entity.ContactMech;

public interface ContactMechRepository extends JpaRepository<ContactMech, Integer> {}