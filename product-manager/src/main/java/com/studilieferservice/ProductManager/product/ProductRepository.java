package com.studilieferservice.ProductManager.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for product which extends JAP repository for basic queries to database
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByNameLike(String name);
}
