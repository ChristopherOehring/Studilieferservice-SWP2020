package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.Gruppe;
import com.studilieferservice.groupmanager.persistence.JpaUserRepository;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * just a normal service for handling JpaRepositories
 * save: saves User to the repository
 * findUser: give it an UserID (String) and you get the User with first- and lastname (if this user exists)
 * getAllUsers: gets you all users
 * getUserById: no difference to findUser - why is it here?
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

    public Optional<User> findUser(String id){
        return userRepository.findById(id);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

}

