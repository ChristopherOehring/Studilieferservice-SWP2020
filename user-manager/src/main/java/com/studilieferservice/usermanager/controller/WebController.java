package com.studilieferservice.usermanager.controller;

import com.studilieferservice.usermanager.user.User;
import com.studilieferservice.usermanager.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.validation.Valid;

@Controller
public class WebController {

    private final UserService userService;

    @Autowired
    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String showIndexPage(){

        return "index";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {

        model.addAttribute("user", new User());
        return "views/registerForm";
    }

    @GetMapping("/editaccount")
    public String editForm(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        return "views/editaccount";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {

        model.addAttribute("user", new User());
        return "views/login";
    }

    @GetMapping("/logout")
    public String logoutForm(Model model) {
        userService.getCurrentUser().setSignedIn(false);
        model.addAttribute("user", userService.getCurrentUser());
        return "views/login";
    }

    @PostMapping("/register")
    public String registerUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            return "views/regerror";
        }
        userService.createUser(user);
        return "views/successedRegistration";
    }

    @PostMapping("/edit")
    public String editUser(@Valid User user, BindingResult bindingResult, Model model) {

        userService.edit(user);
        return "views/successedLogin";
    }

    @GetMapping("/loginn")
    public String loginUser(@Valid User user, BindingResult bindingResult, Model model) {
        if (userService.login(user) == true)
            return "views/successedLogin";
        else
            return "views/regerror";
    }

    @PutMapping("/logoutt")
    public String logout(@Valid User user, BindingResult bindingResult, Model model) {
        if (userService.logout())
            return "views/login";
        else {
            return "views/regerror";
        }
    }

    @GetMapping("/about")
    public String about()
   {
       return "views/about";
   }

    @GetMapping ("/groupmanager")
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
