package com.studilieferservice.ProductManager.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service fir handling JPA Repository
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;



    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository= productRepository;
    }

    /**
     * This method saves the newly created product
     * @param product the new product which is to be saved
     * @return the saved product
     */

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    /**
     * This method deletes a product
     * @param name of the product to be deleted
     */

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

    /**
     *Lists all the products in the database,nice way to see what products are in warehouse
     * @return list of products
     */

    public List<Product> listAllProducts() {
        return productRepository.findAll();
    }
}








