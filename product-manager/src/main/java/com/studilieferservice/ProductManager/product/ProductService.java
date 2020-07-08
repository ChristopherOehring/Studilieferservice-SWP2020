package com.studilieferservice.ProductManager.product;

import com.studilieferservice.ProductManager.kafka.product.ProductEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling JPA Repository
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public ProductService(ProductRepository productRepository, ApplicationEventPublisher eventPublisher) {
        this.productRepository= productRepository;
        this.eventPublisher = eventPublisher;
    }

    /**
     * This method saves the newly created product
     * @param product the new product which is to be saved
     * @return the saved product
     */
    public Product createProduct(Product product) {
        Product saved = productRepository.save(product);
        eventPublisher.publishEvent(new ProductEvent(saved,this));
        return saved;
    }

    /**
     * This method deletes a product
     * @param name of the product to be deleted
     */
    //delete is not published to kafka atm
    public void deleteProductById(String name) {
        productRepository.deleteById(name);
    }

    /**
     * Finds a product
     * @param name of the product to be searched
     * @return the found product
     */
    public Product findProductById(String name) {
        return productRepository.findById(name).orElseThrow();
    }

    public List<Product> findAllWithName(String name) {
        return productRepository.findByNameLike("%"+name+"%");
    }

    /**
     * Lists all the products in the database, nice way to see what products are in warehouse
     * @return list of products
     */
    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }
}








