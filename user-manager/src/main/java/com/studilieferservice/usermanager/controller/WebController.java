package com.studilieferservice.usermanager.controller;

import com.studilieferservice.usermanager.user.User;
import com.studilieferservice.usermanager.user.UserRepository;
import com.studilieferservice.usermanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Arrays;

/**
 * provides a web-controller
 * A web controller returns html documents and is meant to be consumed via browser
 */
@RequestMapping("/web/usermanager")
@Controller
public class WebController {

    private final UserService userService;
    private final UserRepository userRepository;

    @Autowired
    public WebController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/fwd")
    public String fwd(Model model, HttpServletRequest request) {
        System.out.println(request.getRequestURL());
        System.out.println(request.getServerName());
        model.addAttribute("link", request.getServerName());
        //return new RedirectView("http://" + request.getServerName() + ":9080/web/usermanager/index");
        return "redirect";
    }

    /**
     * get request on / invokes the index.html of the user-manager
     *
     * @param
     * @return returns "index" which results in invocation of the index.html
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * get request on /register invokes the registerForm.html of the user-manager
     *
     * @param model
     * @return returns "registerForm" which results in invocation of the registerForm.html
     */
    @GetMapping("/register")
    public String registerForm(Model model, HttpServletRequest request) {

        model.addAttribute("user", new User());
        model.addAttribute("link", request.getServerName());
        return "views/registerForm";
    }

    /**
     * Meant to be used by a webpage to create a new user
     *
     * @param bindingResult
     * @param user
     * @return HTML view
     */
    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult) {
        boolean isCreated = userService.createUser(user);
        if (bindingResult.hasErrors() || !isCreated) {
            return "redirect:regerror";
        }
        return "redirect:login";
    }

    /**
     * get request on /editaccount invokes the editaccount.html of the user-manager
     *
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
     *
     * @param model
     * @return returns "login" which results in invocation of the login.html
     */
    @GetMapping("/login")
    public String loginForm(Model model, HttpServletRequest request) {

        model.addAttribute("user", new User());
        model.addAttribute("link", request.getServerName());
        System.out.println("calling login page");
        return "views/login";
    }

    /**
     * @param model
     * @return returns "login" which results in invocation of the login.html
     */
    @GetMapping("/logout")
    public String logoutForm(Model model,
                             HttpServletResponse response,
                             HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie c = Arrays.stream(cookies).filter(cookie -> cookie.getName()
                .equals("useremail")).findFirst().orElse(null);
        if (!(c == null)) {

            c.setMaxAge(0);
            response.addCookie(c);
        }
        return "redirect:index";
    }

    /**
     * Meant to be used by a webpage to login
     *
     * @return HTML view
     */
    @GetMapping("/loginFwd")
    public RedirectView loginUser(@RequestParam(name = "email") @Nullable String email,
                                  @RequestParam(name = "password") @Nullable String password,
                                  HttpServletRequest request,
                                  HttpServletResponse response) {
        System.out.println(request.getQueryString());
        System.out.println(email + ", " + password);
        if (userService.login(email, password)) {
            Cookie c = new Cookie("useremail", email);
            c.setPath("/");
            response.addCookie(c);
            System.out.println("set cookie value: " + email);
            System.out.println("redirecting: http://" + request.getServerName() + ":9010/web/groupmanager/userMenu");
            return new RedirectView("http://" + request.getServerName() + ":9010/web/groupmanager/userMenu");
        } else {
            RedirectView redirectView = new RedirectView("/login");
            redirectView.setContextRelative(true);
            return redirectView;
        }
    }

    @GetMapping("/regerror")
    public String regerror(Model model) {
        return "views/regerror";
    }

    /**
     * Meant to be used by a webpage to edit user profile
     *
     * @param user
     * @return HTML view
     */

    @PostMapping("/edit")
    public RedirectView editUser(@Valid User user, HttpServletRequest request) {

        userService.edit(user);
        return new RedirectView("http://" + request.getServerName() + ":9010/web/groupmanager/userMenu");
    }

    /**
     * @return HTML view
     */
    @GetMapping("/about")
    public String about() {
        return "views/about";
    }

}
