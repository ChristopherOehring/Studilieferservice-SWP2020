package com.studilieferservice.ProductManager;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
@Repository
@Transactional
public interface ProductRepository extends JpaRepository<Product, String> {

    List<Product> findByNameLike(String name);


}
