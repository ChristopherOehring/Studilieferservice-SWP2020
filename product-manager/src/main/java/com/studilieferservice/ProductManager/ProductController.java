package com.studilieferservice.ProductManager;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }


    @RequestMapping("/product-rest-create")
    @PostMapping
    public ResponseEntity<?> newProduct(@RequestBody Product product) {
        if (product == null) {
            return ResponseEntity.badRequest().body("Product is Null");
        } else
            return ResponseEntity.ok(productService.createProduct(product));

    }


    @RequestMapping("/product-rest-search")
    @GetMapping
    public Product searchProduct(@RequestBody Product product) {


        return productService.findP(product.name);
    }


    @RequestMapping("/product-rest-listall")
    @GetMapping
    public List<Product> listAllProducts() {


        return (productService.listAll());
    }

    @RequestMapping("/product-rest-delete")
    @PostMapping
    public void deleteProduct(@RequestBody Product product) {

         productService.delete(product.name);

    }

}
