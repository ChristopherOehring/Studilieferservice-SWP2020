package com.studilieferservice.usermanager.user;

import com.studilieferservice.usermanager.kafka.user.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.studilieferservice.usermanager.kafka.user.UserEventType;

/**
 * just a normal service for handling JpaRepositories
 */

@Service
public class UserService {
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Autowired
    public UserService(ApplicationEventPublisher eventPublisher, UserRepository userRepository) {
        this.eventPublisher = eventPublisher;
        this.userRepository = userRepository;
    }

    /**
     * saves users to the repository and publish an userEvent
     *
     * @param user
     * @return the saved user
     */
    public boolean createUser(User user) {

        user.setPassword(encoder.encode(user.getPassword()));

        if(!(userRepository.existsById(user.getEmail()))){
            User saved =  userRepository.save(user) ;
            eventPublisher.publishEvent(new UserEvent(saved, this, UserEventType.NEW));
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * fetch if the email in DB exist and then
     * matches the given password
     * if all matches, then return true
     *
     * @param email The given email
     * @param password The given password
     * @return true if the password and the email correct, otherwise false
     */
    public boolean login(String email, String password) {
        System.out.println("Login attempt: "  + email);
        User user1 = userRepository.findById(email).orElse(null);

        if (user1 == null || !encoder.matches(password, user1.getPassword())) {
            System.out.println(user1);
            System.out.println(password);
            System.out.println("denied!");
            return false;
        } else {
            System.out.println("success!");
            return true;
        }
    }

    /**
     * update the given user date in DB
     *
     * @param user
     * @return the updated user
     */
    public User edit(String email, User user) {
        User u = userRepository.findById(email).orElseThrow();

        u.setFirstName(user.getFirstName());
        u.setLastName(user.getLastName());
        u.setUserName(user.getUserName());
        u.setStreet(user.getStreet());
        u.setCity(user.getCity());
        u.setZip(user.getZip());

        eventPublisher.publishEvent(new UserEvent(u,this, UserEventType.UPDATE));
        return userRepository.save(u);
    }

    public User getUser(String email) {
        return userRepository.findById(email).orElseThrow();
    }
}
