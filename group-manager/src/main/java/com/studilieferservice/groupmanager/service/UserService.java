package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.JpaUserRepository;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * just a normal service for handling JpaRepositories
 * save: saves User to the repository
 * getAllUsers: gets you all users
 * getUserById: give it an UserID (String) and you get the User with first- and lastname (if this user exists)
 * deleteUserById: removes an user from the repository when given the e-mail-address of the specific user
 */
@Service
public class UserService {
    private final JpaUserRepository userRepository;

    @Autowired
    public UserService(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

}

