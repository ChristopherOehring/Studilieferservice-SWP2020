package com.studilieferservice.usermanager;

import com.studilieferservice.usermanager.controller.UserController;
import com.studilieferservice.usermanager.user.User;
import com.studilieferservice.usermanager.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Seraj Hadros
 * @version 1.1 7/01/20
 */
public class UserControllerTest {

    @InjectMocks
    private UserController userController;

    @Mock
    private UserService userService;


    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    User user = new User("Muster", "Muster", "Muster",
            "Ilmenau", "Am Markt", "32323",
            "muster@web.com", "ss123456");

    @Test
    public void newUserTest() {

        Mockito.when(userService.createUser(user)).thenReturn(true);
        ResponseEntity<?> response = userController.newUser(user);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(true);

    }

    @Test
    public void editTest() {
        Mockito.when(userService.edit(user.getEmail(), user)).thenReturn(user);
        ResponseEntity<?> response = userController.edit(user);
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo(user);
    }

    @Test
    public void loginTest() {
        Mockito.when(userService.login(user.getEmail(), user.getPassword()))
                .thenReturn(true);
        ResponseEntity<?> response = userController.login(user.getEmail(), user.getPassword());
        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isEqualTo("Successful login");
    }

}
