package com.studilieferservice.usermanager;

import com.studilieferservice.usermanager.user.User;
import com.studilieferservice.usermanager.user.UserRepository;
import com.studilieferservice.usermanager.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Seraj Hadros
 * @version 1.1 7/01/20
 */

public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ApplicationEventPublisher eventPublisher;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    User user = new User("Muster", "Muster", "Muster",
            "Ilmenau", "Am Markt", "32323",
            "muster@web.com", "ss123456");
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Test
    public void createUserTest() {

        Mockito.when(userRepository.save(user)).thenReturn(user);

        boolean response = userService.createUser(user);
        assertThat(response).isEqualTo(true);
    }

    @Test
    public void loginTest() {
        String password = "ss123456";
        user.setPassword(encoder.encode(user.getPassword()));
        Mockito.when(userRepository.findById(user.getEmail()))
                .thenReturn(java.util.Optional.ofNullable(user));
        boolean response = userService.login(user.getEmail(), password);
        assertThat(response).isEqualTo(true);
    }

    @Test
    public void editTest() {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        Mockito.when(userRepository.save(user)).thenReturn(user);
        user.setPassword(encoder.encode(user.getPassword()));
        Mockito.when(userRepository.findById(user.getEmail())).thenReturn(java.util.Optional.ofNullable(user));
        assertThat(userService.login(user.getEmail(), "ss123456")).isEqualTo(true);
        assertThat(userService.getUser(user.getEmail())).isEqualTo(user);
        assertThat(userService.edit(user.getEmail(), user)).isEqualTo(user);
    }
}
