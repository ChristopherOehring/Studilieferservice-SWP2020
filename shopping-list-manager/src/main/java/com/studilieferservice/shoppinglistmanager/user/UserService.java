package com.studilieferservice.shoppinglistmanager.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUserFromKafka(String email, String username) {
        User u = new User(email, username);
        return createUser(u);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    //email is id
    public User getUser(String id) {
        return userRepository.findById(id).orElseThrow();
    }

    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    //for converting the string users from GroupKafka to User objects (does not create new users)
    //the users do not need to be saved because users must already exist to be members of a group
    public List<User> convertUsersFromStringToObject(List<String> membersString) {
        List<User> membersUser = new ArrayList<>();

        for (String s : membersString) {
            membersUser.add(userRepository.findById(s).orElseThrow());
        }

        return membersUser;
    }

    public void updateUserFromKafka(String email, String username) {
        User u = userRepository.findById(email).orElseThrow();
        u.setName(username);
        userRepository.save(u);
    }
}
