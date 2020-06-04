package com.studilieferservice.usermanager.controller;

import com.studilieferservice.usermanager.userService.User;
import com.studilieferservice.usermanager.userService.UserRepository;
import com.studilieferservice.usermanager.userService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import javax.validation.Valid;
/**
 * provides a web-controller
 * A web controller returns html documents and is meant to be consumed via browser
 */

@Controller
public class WebController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public WebController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }
    /**
     * get request on / invokes the index.html of the user-manager
     * @param
     * @return returns "index" which results in invocation of the index.html
     */

    @GetMapping("/")
    public String showIndexPage() {

        return "index";
    }

    /**
     * get request on /register invokes the registerForm.html of the user-manager
     * @param model
     * @return returns "registerForm" which results in invocation of the registerForm.html
     */
    @GetMapping("/register")
    public String registerForm(Model model) {

        model.addAttribute("user", new User());
        return "views/registerForm";
    }

    /**
     * get request on /editaccount invokes the editaccount.html of the user-manager
     * @param model
     * @return returns "editaccount" which results in invocation of the editaccount.html
     */
    @GetMapping("/editaccount")
    public String editForm(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "views/editaccount";
    }

    /**
     * get request on /login invokes the login.html of the user-manager
     * @param model
     * @return returns "login" which results in invocation of the login.html
     */
    @GetMapping("/login")
    public String loginForm(Model model) {

        model.addAttribute("user", new User());
        return "views/login";
    }

    /**
     *
     * @param model
     * @return returns "login" which results in invocation of the login.html
     */
    @GetMapping("/logout")
    public String logoutForm(Model model) {
        userService.getCurrentUser().setSignedIn(false);
        userRepository.save(userService.getCurrentUser());
        model.addAttribute("user", userService.getCurrentUser());

        return "views/login";
    }

    /**
     * Meant to be used by a webpage to create a new user
     * @param bindingResult
     * @param model
     * @param  user
     *
     * @return HTML view
     */

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            return "views/regerror";
        }
        userService.createUser(user);
        return "views/successedRegistration";
    }

    /**
     * Meant to be used by a webpage to edit user profile
     * @param user
     * @param bindingResult
     * @param model
     * @return HTML view
     */
    @PostMapping("/edit")
    public String editUser(@Valid User user, BindingResult bindingResult, Model model) {

        userService.edit(user);
        return "views/successedLogin";
    }

    /**
     * Meant to be used by a webpage to login
     * @param user
     * @param bindingResult
     * @param model
     * @return HTML view
     */
    @GetMapping("/loginn")
    public String loginUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (userService.login(user) == true)
            return "views/successedLogin";
        else
            return "views/regerror";
    }

    /**
     *
     * @return HTML view
     */
    @GetMapping("/about")
    public String about() {
        return "views/about";
    }

    /**
     *
     * @return
     */
    @GetMapping("/groupmanager")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8010/web/group/index");
        return redirectView;
    }

/*    @GetMapping ("/shoppingList")
    public RedirectView localRedirect2() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8010/web/group/index");
        return redirectView;
    }*/
}
