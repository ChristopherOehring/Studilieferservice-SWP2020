package com.studilieferservice.ProductManager.controller;

import com.studilieferservice.ProductManager.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductWebController {

    private final ProductService productService;

    @Autowired
    public ProductWebController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public String listProducts(Model model, @RequestParam(defaultValue = "") String name) {
        if (!name.equals("")) {
            model.addAttribute("products", productService.findAllWithName(name));
        } else {
            model.addAttribute("products", productService.listAllProducts());
        }
        return "index";
    }
}
