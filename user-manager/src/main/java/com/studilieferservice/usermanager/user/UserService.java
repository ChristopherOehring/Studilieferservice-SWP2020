package com.studilieferservice.usermanager.user;

import com.studilieferservice.usermanager.kafka.user.UserEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static com.studilieferservice.usermanager.kafka.user.UserEventType.NEW;
import static com.studilieferservice.usermanager.kafka.user.UserEventType.UPDATE;

/**
 * just a normal service for handling JpaRepositories
 */

@Service
public class UserService {
    private final ApplicationEventPublisher eventPublisher;
    private final UserRepository userRepository;
    private User currentUser;
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
    public User createUser(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        setCurrentUser(null);
        User saved = userRepository.save(user);

        eventPublisher.publishEvent(new UserEvent(user, this, NEW));
        return saved;
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
        User user1 = findOne(email);
        //userRepository.findByEmail(user.getEmail());

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
     * update the login user state to false in DB
     *
     * @return true if the update success was, otherwise false
     */
    public boolean logout() {

        currentUser.setSignedIn(false);
        userRepository.save(this.currentUser);
        if (currentUser.isSignedIn() == false) {
            return true;
        } else
            return false;
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
        currentUser.setSignedIn(true);
        currentUser.setPassword(currentUser.getPassword());
        currentUser.setEmail(currentUser.getEmail());
        eventPublisher.publishEvent(new UserEvent(currentUser,this, UPDATE));
        return userRepository.save(currentUser);
    }

    /**
     * fetch the user with the given email from DB
     *
     * @param email
     * @return user if exist in DB, otherwise null.
     */
    public User findOne(String email) {
        return userRepository.findById(email).orElse(null);
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
