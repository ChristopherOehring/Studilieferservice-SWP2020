package com.studilieferservice.shoppinglistmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @ComponentScan.Filter(
		type=FilterType.REGEX,
		pattern="com\\.studilieferservice\\.shoppinglistmanager\\.kafka\\..*|" +
				"com\\.studilieferservice\\.shoppinglistmanager\\.ShoppingListManagerApplication.*"))

public class ShoppingListManagerApplicationWithoutKafka {

	public static void main(String[] args) {
		SpringApplication.run(ShoppingListManagerApplicationWithoutKafka.class, args);
	}

}
