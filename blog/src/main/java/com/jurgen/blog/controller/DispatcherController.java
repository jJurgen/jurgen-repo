package com.jurgen.blog.controller;

import com.jurgen.blog.domain.Post;
import com.jurgen.blog.domain.User;
import com.jurgen.blog.formbeans.SignUpFormBean;
import com.jurgen.blog.formbeans.WritePostFormBean;
import com.jurgen.blog.sevice.PostService;
import com.jurgen.blog.sevice.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DispatcherController {
    
    private final static int RECENT_POST_COUNT = 5;
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    @RequestMapping(value = {"/", "/home"}, method = RequestMethod.GET)
    public String toHomePage(Model model) {
        List<Post> posts = postService.getRecentPosts(RECENT_POST_COUNT);
        model.addAttribute("posts", posts);
        return "home";
    }
    
    @RequestMapping(value = {"/signUp", "/signup"}, method = RequestMethod.GET)
    public String toSignUpPage(Model model) {
        model.addAttribute("signUpForm", new SignUpFormBean());
        return "signUp";
    }
    
    @RequestMapping(value = {"/signIn", "/signin"}, method = RequestMethod.GET)
    public String toSignInPage() {
        return "signIn";
    }
    
    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public String toSearchingPage() {
        return "search";
    }
    
    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String toProfilePage() {
        return "profile";
    }
    
    @RequestMapping(value = "/profile{username}", method = RequestMethod.GET)
    public ModelAndView toProfile(@PathVariable String username, ModelAndView mav) {
        User user = (User) userService.loadUserByUsername(username);
        mav.addObject("user", user);
        mav.setViewName("profile");
        return mav;
    }
    
    @RequestMapping(value = {"/editPost", "/editpost"}, method = RequestMethod.GET)
    public String toEditPostPage() {
        return "editPost";
    }
    
    @RequestMapping(value = {"/editProfile", "/editprofile"}, method = RequestMethod.GET)
    public String toEditProfilePage() {
        return "editProfile";
    }
    
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    public String toAdminPage() {
        return "admin";
    }
    
    public PostService getPostService() {
        return postService;
    }
    
    public void setPostService(PostService postService) {
        this.postService = postService;
    }
    
    public UserService getUserService() {
        return userService;
    }
    
    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
}
