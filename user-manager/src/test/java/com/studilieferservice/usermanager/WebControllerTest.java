package com.studilieferservice.usermanager;

import com.studilieferservice.usermanager.controller.WebController;
import com.studilieferservice.usermanager.user.User;
import com.studilieferservice.usermanager.user.UserRepository;
import com.studilieferservice.usermanager.user.UserService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Seraj Hadros
 * @version 1.1 7/01/20
 */
public class WebControllerTest {

    @InjectMocks
    private WebController webController;

    @Mock
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(webController).build();
    }

    User user = new User("Muster", "Muster", "Muster",
            "Ilmenau", "Am Markt", "32323",
            "muster@web.com", "ss123456");

    @Test
    public void webControllerTest() throws Exception {
        assertThat(webController).isNotNull();
    }


    @Test
    public void fwdTest() throws Exception {
        this.mockMvc.perform(get("/web/usermanager/fwd"))
                .andExpect(model().attribute("link", instanceOf(String.class)))
                .andExpect(view().name("redirect"));
    }

    @Test
    public void loginUserTest() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        String email = user.getEmail();
        String password = user.getPassword();
        when(userService.login(email, password)).thenReturn(true);
        this.mockMvc.perform(get("/loginFwd")
                .param("email", email)
                .param("password", password));
        RedirectView myResponse = webController.loginUser(email, password, request, response);
        assertThat(myResponse.getUrl()).isNotEqualTo("/login");
    }

    @Test
    public void logoutFormTest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Model model = mock(Model.class);

        Cookie myCookie = new Cookie("useremail", user.getEmail());
        Cookie[] myCookies = {myCookie};
        when(request.getCookies()).thenReturn(myCookies);
        String myResponse = webController.logoutForm(model, response, request);
        assertThat(myResponse).isEqualTo("redirect:index");

    }

    @Test
    public void registerFormTest() throws Exception {
        this.mockMvc.perform(get("/web/usermanager/register"))
                .andExpect(model().attribute("link", instanceOf(String.class)))
                .andExpect(model().attribute("user", instanceOf(User.class)))
                .andExpect(view().name("views/registerForm"));
    }

    @Test
    public void editFormTest() throws Exception {
        when(userService.getCurrentUser())
                .thenReturn(user);
        this.mockMvc.perform(get("/web/usermanager/editaccount"))
                .andExpect(model().attribute("user", user))
                .andExpect(view().name("views/editaccount"));
    }

    @Test
    public void loginFormTest() throws Exception {
        this.mockMvc.perform(get("/web/usermanager/login"))
                .andExpect(model().attribute("link", instanceOf(String.class)))
                .andExpect(model().attribute("user", instanceOf(User.class)))
                .andExpect(view().name("views/login"));
    }

    @Test
    public void regerrorTest() throws Exception {
        this.mockMvc.perform(get("/web/usermanager/regerror"))
                .andExpect(view().name("views/regerror"))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void aboutTest() throws Exception {
        this.mockMvc.perform(get("/web/usermanager/about"))
                .andExpect(view().name("views/about"))
                .andExpect(status().is2xxSuccessful());

    }

    @Test
    public void registerUserTest() throws Exception {

        BindingResult bindingResult = mock(BindingResult.class);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(userService.createUser(user)).thenReturn(true);

        assertThat(webController.registerUser(user, bindingResult)).isEqualTo("redirect:login");

    }


    @Test
    public void editUserTest() throws Exception {
        this.mockMvc.perform(post("/web/usermanager/edit")
                .param("firstName", "Muster")
                .param("lastName", "Muster")
                .param("userName", "Muster")
                .param("city", "Muster")
                .param("street", "Muster")
                .param("zip", "21221")
                .param("email", "muster@web.com")
                .param("password", "ss123456"))

                .andExpect(status().is3xxRedirection());
    }
}
