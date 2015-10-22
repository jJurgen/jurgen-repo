package com.jurgen.chat.controllers;

import com.google.gson.Gson;
import com.jurgen.chat.entities.Message;
import com.jurgen.chat.entities.User;
import com.jurgen.chat.services.MessageService;
import com.jurgen.chat.services.UserService;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class MainController {

    @Resource(name = "messageService")
    private MessageService messageService;

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView toChatPage(ModelAndView model) {
        model.setViewName("chat");
        return model;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(@RequestParam(value = "message") String message, HttpSession session) {
        User currentUser = (User) session.getAttribute("currentUser");
        messageService.addMessage(new Message(message, currentUser.getNickname()));
        Gson gson = new Gson();
        String json = gson.toJson("success");
        return json;
    }

    @RequestMapping(value = "/signOut", method = RequestMethod.GET)
    public String signOut(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/";
    }

    @RequestMapping(value = "/SignIn", method = RequestMethod.GET)
    public String toSignInPage() {
        return "signIn";
    }

    @RequestMapping(value = "/Register", method = RequestMethod.GET)
    public String toRegisterPage() {
        return "register";
    }

    @RequestMapping(value = "/RegisterUser", method = RequestMethod.POST)
    public String registerUser(@RequestParam(value = "nickname") String nickname,
            @RequestParam(value = "password") String password) {
        if (userService.addUser(nickname, password)) {
            return "redirect:/";
        } else {
             return "redirect:/Register";
        }
    }

    @RequestMapping(value = "/checkUser", method = RequestMethod.POST)
    public String checkUser(@RequestParam(value = "nickname") String nickname,
            @RequestParam(value = "password") String password, HttpSession session) {
        User user = userService.signIn(nickname, password);
        if (user != null) {
            session.setAttribute("currentUser", user);
            return "redirect:/";
        }
        return "redirect:/SignIn";
    }

    @RequestMapping(value = "/getMessages", method = RequestMethod.POST)
    @ResponseBody
    public String getMessages() {
        List<Message> messages = messageService.getMessages();
        Gson gson = new Gson();
        String json = gson.toJson(messages);
        return json;
    }

    public MessageService getMessageService() {
        return messageService;
    }

    public void setMessageService(MessageService messageService) {
        this.messageService = messageService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
