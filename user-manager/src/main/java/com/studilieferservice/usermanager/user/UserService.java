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
    private User currentUser ;
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
        setCurrentUser(null);

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
            currentUser = user1;
            return true;
        }
    }

    /**
     * update the given user date in DB
     *
     * @param user
     * @return the updated user
     */
    public User edit(User user) {

        currentUser.setFirstName(user.getFirstName());
        currentUser.setLastName(user.getLastName());
        currentUser.setUserName(user.getUserName());
        currentUser.setStreet(user.getStreet());
        currentUser.setCity(user.getCity());
        currentUser.setZip(user.getZip());
        currentUser.setPassword(currentUser.getPassword());
        currentUser.setEmail(currentUser.getEmail());

        eventPublisher.publishEvent(new UserEvent(currentUser,this, UserEventType.UPDATE));
        return userRepository.save(currentUser);
    }

    /**
     * to get the current user
     *
     * @return user
     */
    public User getCurrentUser() {
        return currentUser;
    }

    /**
     * to set the current user variable
     *
     * @param currentUser
     */
    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
