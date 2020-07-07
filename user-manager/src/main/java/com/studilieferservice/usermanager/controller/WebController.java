package com.studilieferservice.usermanager.controller;

import com.studilieferservice.usermanager.user.User;
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

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
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
     * @return returns "index" which results in invocation of the index.html
     */
    @GetMapping("/index")
    public String index() {
        return "index";
    }

    /**
     * get request on /register invokes the registerForm.html of the user-manager
     *
     * @param model a Autowired Object of class "Model", which is then used by thymeleaf
     * @param request Contains all kinds of information about how this method was called (through a browser)
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
     * @param user The user that should be registred
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
     * @param model a Autowired Object of class "Model", which is then used by thymeleaf
     * @param email the userEmail, as found in the "useremail" cookie
     * @param request Contains all kinds of information about how this method was called (through a browser)
     * @return returns "editaccount" which results in invocation of the editaccount.html
     */
    @GetMapping("/editaccount")
    public String editForm(Model model, @CookieValue("useremail") @Nullable String email, HttpServletRequest request) {
        model.addAttribute("user", userService.getUser(email));
        model.addAttribute("link", request.getServerName());
        return "views/editaccount";
    }

    /**
     * get request on /login invokes the login.html of the user-manager
     *
     * @param model a Autowired Object of class "Model", which is then used by thymeleaf
     * @param request Contains all kinds of information about how this method was called (through a browser)
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
     * Deletes the cookie which represents the user which is logged in
     *
     * @param response Can be used to set properties of the http response
     * @param request Contains all kinds of information about how this method was called (through a browser)
     * @return returns "login" which results in invocation of the login.html
     */
    @GetMapping("/logout")
    public String logoutForm(HttpServletResponse response,
                             HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        Cookie c = Arrays.stream(cookies).filter(cookie -> cookie.getName()
                .equals("useremail")).findFirst().orElse(null);
        if (!(c == null)) {
            c.setValue("");
            c.setPath("/");
            c.setMaxAge(0);
            response.addCookie(c);
        }
        return "redirect:index";
    }

    /**
     * Meant to be used by a webpage to login. This method checks the login data. If it is correct it will set the cookie "useremail", which is used to store the login and
     * then redirect to the userMenu. Otherwise it redirects to the login page.
     *
     * @param email the user email
     * @param password the user passwor
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
     * @param user a user Object with the edited information
     * @param email the email of the user, that should be edited
     * @return HTML view
     */
    @PostMapping("/edit")
    public RedirectView editUser(@Valid User user, HttpServletRequest request, @CookieValue("useremail") @Nullable String email) {

        userService.edit(email, user);
        return new RedirectView("http://" + request.getServerName() + ":9010/web/groupmanager/userMenu");
    }

    /**
     * @return HTML view: about
     */
    @GetMapping("/about")
    public String about() {
        return "views/about";
    }

    /**
     *
     * @return
     */
    @Deprecated
    @GetMapping("/groupmanager")
    public RedirectView localRedirect() {
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl("http://localhost:8010/web/group/index");
        return redirectView;
    }
}
