package com.studilieferservice.usermanager.user;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, String> {
    User findByEmail(String email);
    User findByUserName(String userName);

}