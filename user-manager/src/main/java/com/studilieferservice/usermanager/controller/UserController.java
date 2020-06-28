package com.studilieferservice.usermanager.controller;

import com.studilieferservice.usermanager.user.User;
import com.studilieferservice.usermanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * provides a api for the UserService
 */
@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * this Method is used to create a new User and save it in DB
     * can be reached with a POST request at /rest-register
     *
     * @param user in JSON format.
     *
     * @return ResponEntity with statusCode with or without the created user,
     * that depends on the result of the isValidUser().
     */
    @PostMapping("/rest-register")
    public ResponseEntity<?> newUser(@RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest().body("User is Null");
        }
        if (isValidUser(user)) {
            return ResponseEntity.badRequest().body("User values invalid");
        } else

            return ResponseEntity.ok(userService.createUser(user));
    }

    /**
     * used in the newUser Method
     * @param user
     * @return true if the user is valid, otherwise false
     */
    private boolean isValidUser( User user) {
        return StringUtils.isEmpty(user.getFirstName())
                || StringUtils.isEmpty(user.getLastName())
                || StringUtils.isEmpty(user.getUserName())
                || StringUtils.isEmpty(user.getStreet())
                || StringUtils.isEmpty(user.getCity())
                || StringUtils.isEmpty(user.getZip())
                || StringUtils.isEmpty(user.getEmail())
                || StringUtils.isEmpty(user.getPassword());
    }

    /**
     * this method is used to edit the user profile.
     * can be reached with the POST request at /rest-edit
     *
     * @param user in JSON format.
     *
     * @return ResponseEntity with statusCode with or without the created user,
     * that depends on the result of the isUserDataValid().
     */
    @PostMapping("/rest-edit")
    public ResponseEntity<?> edit(@RequestBody User user) {

        if (user == null) {
            return ResponseEntity.badRequest().body("User is Null");
        }
        if (isUserDataValid(user)) {
            return ResponseEntity.badRequest().body("User values invalid");
        } else
            return ResponseEntity.ok(userService.edit(user));
    }

    /**
     * used in the edit Method
     *
     * @param user
     *
     * @return true if the new Data for user is valid, otherwise false
     */
    private boolean isUserDataValid( User user) {
        return StringUtils.isEmpty(user.getFirstName())
                || StringUtils.isEmpty(user.getLastName())
                || StringUtils.isEmpty(user.getUserName())
                || StringUtils.isEmpty(user.getStreet())
                || StringUtils.isEmpty(user.getCity())
                || StringUtils.isEmpty(user.getZip());
    }
//
//    /**
//     * this method is used to login
//     * can be reached with the GET request at /rest-login
//     *
//     * @param user
//     *
//     * @return ResponseEntity with CodeStatus ok if the user is registered
//     * and the email and password are correct.
//     */
//    @GetMapping("/rest-login")
//    public ResponseEntity<?> login(@RequestBody User user) {
//        if (userService.login(user.getEmail(), user.getPassword()) == true)
//            return ResponseEntity.ok("OKKKKKK");
//        else
//            return ResponseEntity.notFound().build();
//    }
//
//    /**
//     * this method is used to logout the current user
//     * @param
//     * @return ResponseEntity CodeStatus
//     */
//    @PutMapping("/rest-logout")
//    public ResponseEntity<?> logout() {
//        if (true)
//            return ResponseEntity.accepted().build();
//        else
//            return ResponseEntity.badRequest().build();
//    }
}
