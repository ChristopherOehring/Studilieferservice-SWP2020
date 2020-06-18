package com.studilieferservice.ProductManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository= productRepository;

    }


    public Product createProduct(Product product) {

        return productRepository.save(product);

    }

    public void delete(String name) {

        productRepository.deleteById(name);

    }
    public Product findP(String name) {
      return productRepository.findById(name).orElse(null);
    }
    public List<Product> findByName(String name) {

        return  productRepository.findByNameLike("%"+name+"%");
    }

    public List<Product> listAll() {
       // List<Product> products = new ArrayList<>();
        //productRepository.findAll().forEach(products::add);
        return productRepository.findAll();
    }



}








