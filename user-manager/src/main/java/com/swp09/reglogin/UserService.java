package com.swp09.reglogin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class UserService {
    //Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private final UserRepository userRepository;

    public User currentUser;
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

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

        if (user1.equals(null) && !encoder.matches(user.getPassword(), user1.getPassword())) {
            return false;
        } else {
            user1.setSignedIn(true);
            userRepository.save(user1);
            currentUser = user1;
          //logger.info(currentUser.getFirstName());
            return true;
        }

    }

    public boolean logout() {

          //logger.info(currentUser.getEmail());
            currentUser.setSignedIn(false);
            userRepository.save(currentUser);
            this.currentUser = null;
            return true;

    }

    public User edit(User user){

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUserName(user.getUserName());
        currentUser.setAddress(user.getAddress());
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

}
