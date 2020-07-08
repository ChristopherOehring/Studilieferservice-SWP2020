package com.studilieferservice.ProductManager.controller;

import com.studilieferservice.ProductManager.product.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/product")
public class ProductWebController {

    private final ProductService productService;

    @Autowired
    public ProductWebController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/listProducts")
    public ModelAndView listProducts(HttpServletRequest request) {
        ModelAndView model = new ModelAndView("products");

        model.addObject("groupId", request.getParameter("groupId"));
        model.addObject("userId", request.getParameter("userId"));
        model.addObject("link", request.getServerName());
        model.addObject("products", productService.listAllProducts());

        return model;
    }
}
