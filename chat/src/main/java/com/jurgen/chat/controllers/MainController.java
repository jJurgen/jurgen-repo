package com.jurgen.chat.controllers;

import com.jurgen.chat.domain.Message;
import com.jurgen.chat.domain.User;
import com.jurgen.chat.domain.UserRole;
import com.jurgen.chat.services.MessageService;
import com.jurgen.chat.services.RoleService;
import com.jurgen.chat.services.UserService;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Resource;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainController {

    private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

    @Resource(name = "messageService")
    private MessageService messageService;

    @Resource(name = "userService")
    private UserService userService;

    @Resource(name = "roleService")
    private RoleService roleService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView toChatPage(ModelAndView model) {
        model.setViewName("chat");
        return model;
    }

    @RequestMapping(value = "/sendMessage", method = RequestMethod.POST)
    @ResponseBody
    public String sendMessage(@RequestParam(value = "message") String message) throws IOException {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User currentUser = null;
        if (principal instanceof User) {
            String username = ((User) principal).getUsername();
            currentUser = (User) userService.loadUserByUsername(username);
        }
        String msg = message.trim().replaceAll(" +", " ");
        ObjectMapper mapper = new ObjectMapper();
        String resp;
        if ((msg.length() != 0) && (currentUser != null)) {
            messageService.addMessage(new Message(msg, currentUser));
            resp = mapper.writeValueAsString("success");
        } else {
            resp = mapper.writeValueAsString("empty message");
        }
        return resp;
    }

    @RequestMapping(value = "/userPage", method = RequestMethod.GET)
    public String toUserPage() {
        return "userPage";
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
            @RequestParam(value = "password") String password,
            @RequestParam(value = "repeatedPassword") String repeatedPassword) {
        if ((nickname.length() != 0) && ((password.length() != 0) && (password.equals(repeatedPassword)))) {
            UserRole role = roleService.getRoleByName("ROLE_USER");
            Set<UserRole> roles = new HashSet<>();
            roles.add(role);
            User newUser = new User(nickname, password, roles);
            userService.addUser(newUser);
        }
        return "redirect:/";
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
