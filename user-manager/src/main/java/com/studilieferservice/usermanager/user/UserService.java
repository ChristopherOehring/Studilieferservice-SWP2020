package com.studilieferservice.usermanager.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Service
public class UserService {

    private final UserRepository userRepository;
    private User currentUser;
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        currentUser = null;
        return userRepository.save(user);
    }

    public boolean login(User user) {

        User user1 = userRepository.findByEmail(user.getEmail());

        if (user1 == null || !encoder.matches(user.getPassword(), user1.getPassword())) {
            return false;
        } else {
            user1.setSignedIn(true);
            userRepository.save(user1);
            currentUser = user1;
            return true;
        }
    }

    public boolean logout() {

        currentUser.setSignedIn(false);
        userRepository.save(this.currentUser);
        if (currentUser.isSignedIn() == false) {

            return true;
        } else
            return false;
    }

    public User edit(User user) {

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUserName(user.getUserName());
        currentUser.setStreet(user.getStreet());
        currentUser.setCity(user.getCity());
        currentUser.setZip(user.getZip());
        currentUser.setSignedIn(true);
        currentUser.setPassword(currentUser.getPassword());
        currentUser.setEmail(currentUser.getEmail());
        return userRepository.save(currentUser);
    }

    public User findOne(String email) {
        return userRepository.findById(email).orElse(null);
    }

    public boolean isUserPresent(String email) {

        User u = userRepository.findById(email).orElse(null);
        if (u != null) {
            return true;
        } else {
            return false;
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
