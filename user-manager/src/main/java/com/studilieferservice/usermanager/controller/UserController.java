package com.studilieferservice.usermanager.controller;

import com.studilieferservice.usermanager.userService.User;
import com.studilieferservice.usermanager.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    private final UserService userService;



    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;

    }

    /**
     * this Method is used to create a new User
     *
     * @param user
     * @return ResponEntity
     */
    @PostMapping("/rest-register")
    public ResponseEntity<?> newUser(@RequestBody User user) {
        if (user == null) {
            return ResponseEntity.badRequest().body("User is Null");
        }
        if (StringUtils.isEmpty(user.getFirstName())
                || StringUtils.isEmpty(user.getLastName())
                || StringUtils.isEmpty(user.getUserName())
                || StringUtils.isEmpty(user.getStreet())
                || StringUtils.isEmpty(user.getCity())
                || StringUtils.isEmpty(user.getZip())
                || StringUtils.isEmpty(user.getEmail())
                || StringUtils.isEmpty(user.getPassword())) {
            return ResponseEntity.badRequest().body("User values invalid");
        } else

            return ResponseEntity.ok(userService.createUser(user));
    }

    /**
     *
     * @param user
     * @return
     */
    @PostMapping("/rest-edit")
    public ResponseEntity<?> edit(@RequestBody User user){

        if (user == null) {
            return ResponseEntity.badRequest().body("User is Null");
        }
        if (StringUtils.isEmpty(user.getFirstName())
                || StringUtils.isEmpty(user.getLastName())
                || StringUtils.isEmpty(user.getUserName())
                || StringUtils.isEmpty(user.getStreet())
                || StringUtils.isEmpty(user.getCity())
                || StringUtils.isEmpty(user.getZip())
                ) {
            return ResponseEntity.badRequest().body("User values invalid");
        } else
            return ResponseEntity.ok(userService.edit(user));
    }

    /**
     *
     * @param user
     * @return Response CodeStatus
     */
    @GetMapping("/rest-login")
    public ResponseEntity<?> login(@RequestBody User user) {
        if (userService.login(user) == true)
            return ResponseEntity.ok("OKKKKKK");
        else
            return ResponseEntity.notFound().build();
    }

    /**
     *
     * @param
     * @return Response CodeStatus
     */
    @PutMapping("/rest-logout")
    public ResponseEntity<?> logout(){
        if (userService.logout() == true)
            return ResponseEntity.accepted().build();
        else
            return ResponseEntity.badRequest().build();
    }

}
