package com.studilieferservice.ProductManager.controller;

import com.studilieferservice.ProductManager.product.ProductService;
import com.studilieferservice.ProductManager.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Provides api interface for Productmanager
 */
@RestController
@RequestMapping("product")
public class ProductRestController {

    private final ProductService productService;

    @Autowired
    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    /**
     * Creates a product and saves to database
     * @param product in JSON format
     * @return response entity, if successful, with the created product
     */
    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody Product product) {
        if (product == null)
            return ResponseEntity.badRequest().body("Product is Null");
        else
            return ResponseEntity.ok(productService.createProduct(product));
    }

    /**
     * Searches all the products with the given name in database
     * @param product in JSON format
     * @return the found product in JSON product
     */
    @GetMapping("/search")
    public Product searchProduct(@RequestBody Product product) {
        return productService.findProductById(product.getName());
    }

    /**
     * Outputs all the products in the database
     * @return an array of JSON objects of type product
     */
    @GetMapping("/listall")
    public List<Product> listAllProducts() {
        return (productService.listAllProducts());
    }

    /**
     * Deletes the product from database
     * @param product in JSON format
     */
    @PostMapping("/delete")
    public void deleteProduct(@RequestBody Product product) {
         productService.deleteProductById(product.getName());
    }
}
