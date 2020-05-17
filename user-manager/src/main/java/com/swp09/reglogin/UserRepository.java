package com.swp09.reglogin;

import org.springframework.data.repository.CrudRepository;


public interface UserRepository extends CrudRepository<User, String> {
    User findByEmail(String email);
    User findByUserName(String userName);

}