package com.studilieferservice.groupmanager.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * just a normal JpaRepository, for more information, see the spring documentation
 * https://docs.spring.io/spring-data/jpa/docs/1.5.0.RELEASE/reference/html/jpa.repositories.html
 */
public interface JpaUserRepository extends JpaRepository<User, String> {
}
