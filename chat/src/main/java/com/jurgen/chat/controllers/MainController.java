package com.jurgen.chat.controllers;

import com.jurgen.chat.domain.Message;
import com.jurgen.chat.domain.User;
import com.jurgen.chat.services.MessageService;
import com.jurgen.chat.services.UserService;
import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

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
    public String sendMessage(@RequestParam(value = "message") String message, HttpSession session) throws IOException {
        User currentUser = (User) session.getAttribute("currentUser");
        String msg = message.trim().replaceAll(" +", " ");
        ObjectMapper mapper = new ObjectMapper();
        String resp;
        if (msg.length() != 0) {
            messageService.addMessage(new Message(msg, currentUser));
            resp = mapper.writeValueAsString("success");
        }
        else{
             resp = mapper.writeValueAsString("empty message");
        }         
        return resp;
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
        userService.addUser(nickname, password);
        return "redirect:/";
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
        ObjectMapper mapper = new ObjectMapper();
        String json;
        try {
            json = mapper.writeValueAsString(messages);
            return json;
        } catch (IOException ex) {
            LOG.error("Can't convert List<Message> to Json with error: " + ex.getMessage());
            return "";
        }
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
