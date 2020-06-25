package com.studilieferservice.groupmanager.service;

import com.studilieferservice.groupmanager.persistence.JpaUserRepository;
import com.studilieferservice.groupmanager.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * just a normal service for handling JpaRepositories<br>
 * getAllUsers: gets you all users<br>
 * getUserById: give it an UserID (String) and you get the User with first- and lastname (if this user exists)<br>
 * deleteUserById: removes an user from the repository when given the e-mail-address of the specific user<br>
 * @author Christopher Oehring
 * @author Manuel Jirsak
 * @version 1.1 6/18/20
 */
@Service
public class UserService {
    private final JpaUserRepository userRepository;

    @Autowired
    public UserService(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * save: saves User to the repository
     * @param user the user that should be saved
     * @return the user that was saved
     */
    public User save(User user) {
        return userRepository.save(user);
    }

    /**
     * This method can be used to find a user
     * @param id the identifying email of the user to be found
     * @return an optional containing the user, or null if none was found.
     */
    public Optional<User> findById(String id){
        return userRepository.findById(id);
    }

    /**
     * This method can be used to get all the users
     * @return a List of all the users
     */
    public List<User> findAll() {
        return userRepository.findAll();
    }

    /**
     * This method can be used to delete a user
     * @param id the identifying email of the user to be found
     */
    public void deleteUserById(String id) {
        userRepository.deleteById(id);
    }

}

