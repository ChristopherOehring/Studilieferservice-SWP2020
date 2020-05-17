package com.swp09.reglogin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootApplication
public class RegLoginApplication {



    public static void main(String[] args) {


        SpringApplication.run(RegLoginApplication.class, args);

    }






}



