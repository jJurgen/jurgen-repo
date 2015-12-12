package com.jurgen.blog.controller;

import com.jurgen.blog.formbeans.SignUpFormBean;
import com.jurgen.blog.sevice.UserService;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AccountController {

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public String createAccount(@Valid @ModelAttribute("signUpForm") SignUpFormBean signUpForm,
            BindingResult result, Model model) {
        boolean uniqueness = true;
        if (result.hasErrors()) {
            System.out.println("Errors count: " + result.getErrorCount());
            return "signUp";
        } else {
            if (userService.isUsernameExists(signUpForm.getUsername())) {
                System.out.println("USERNAME IS NOT UNIQUE!!!");
                model.addAttribute("usernameUniqueness", "Username already exist");
                uniqueness = false;
            }
            if (userService.isEmailExists(signUpForm.getEmail())) {
                System.out.println("EMAIL IS NOT UNIQUE!!!");
                model.addAttribute("emailUniqueness", "Email already exist");
                uniqueness = false;
            }
            if (!uniqueness) {
                return "signUp";
            }
            userService.signUp(signUpForm);
            System.out.println("no Errors!");
        }
        return "redirect:home?error";
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
