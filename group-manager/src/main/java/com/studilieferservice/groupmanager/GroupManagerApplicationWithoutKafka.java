package com.studilieferservice.groupmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;

/**
 * Starts the Application without Kafka. For simple testing only
 */

@Configuration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = @ComponentScan.Filter(
		type=FilterType.REGEX,
		pattern="com\\.studilieferservice\\.groupmanager\\.kafka\\..*|" +
				"com\\.studilieferservice\\.groupmanager\\.GroupManagerApplication.*"))

public class GroupManagerApplicationWithoutKafka {

	public static void main(String[] args) {
		SpringApplication.run(GroupManagerApplicationWithoutKafka.class, args);
	}

}
