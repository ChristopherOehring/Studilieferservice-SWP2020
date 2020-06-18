package com.studilieferservice.ProductManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductWebController {

    @Autowired
    private ProductService productService;

    @GetMapping("/products")
    public String listProducts(Model model,@RequestParam(defaultValue ="")String name){
        model.addAttribute("products", productService.findByName(name));
        return "index";
    }

}
