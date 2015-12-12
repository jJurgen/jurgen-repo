package com.jurgen.blog.controller;

import com.jurgen.blog.domain.Post;
import com.jurgen.blog.domain.User;
import com.jurgen.blog.formbeans.AddCommentFormBean;
import com.jurgen.blog.formbeans.WritePostFormBean;
import com.jurgen.blog.sevice.PostService;
import com.jurgen.blog.sevice.UserService;
import java.util.List;
import javax.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PostController {

    @Autowired
    private PostService postService;

    private final Logger LOG = LoggerFactory.getLogger(UserService.class);

    @RequestMapping(value = "/post", method = RequestMethod.GET)
    public String toPostpage() {
        return "post";
    }

    @RequestMapping(value = "/post{postId}", method = RequestMethod.GET)
    public ModelAndView toPostpage(@PathVariable String postId, ModelAndView mav) {
        try {
            Integer id = Integer.parseInt(postId);
            Post post = postService.getPost(id);
            mav.addObject("post", post);
        } catch (NumberFormatException ex) {
            LOG.error("Incorrect postId: " + ex.getMessage());
        }
        mav.setViewName("post");
        return mav;
    }

    @RequestMapping(value = "/addComment", method = RequestMethod.POST)
    public ModelAndView addComment(@Valid @ModelAttribute("addCommentFormBean") AddCommentFormBean addCommentFormBean,
            BindingResult result, ModelAndView mav) {
        Post post = postService.getPost(addCommentFormBean.getPostId());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!result.hasErrors()) {
            postService.addComment(addCommentFormBean.getComment(), user, post);
            mav.setViewName("redirect:post" + post.getId());
            return mav;
        }
        mav.addObject("post", post);
        mav.addObject("addCommentFormBean", addCommentFormBean);
        mav.setViewName("post");
        return mav;
    }

    @RequestMapping(value = {"/writePost", "/writepost"}, method = RequestMethod.GET)
    public String toWritePostPage(Model model) {
        model.addAttribute("writePostFormBean", new WritePostFormBean());
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (user != null) {
            model.addAttribute("posts", user.getPosts());
        }
        return "writePost";
    }

    @RequestMapping(value = "/writePost", method = RequestMethod.POST)
    public String writePost(@Valid @ModelAttribute("writePostFormBean") WritePostFormBean writePostFormBean,
            BindingResult result, Model model) {
        if (!result.hasErrors()) {
            User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            postService.addPost(writePostFormBean.getTitle(), writePostFormBean.getContent(), user);
            List<Post> posts = postService.getUsersPosts(user);
            user.setPosts(posts);
            model.addAttribute("posts", posts);
        }
        System.out.println(writePostFormBean.getTitle() + " " + writePostFormBean.getContent());
        return "redirect:writePost";
    }

}
