package com.jurgen.blog.sevice;

import com.jurgen.blog.dao.CommentDao;
import com.jurgen.blog.dao.PostDao;
import com.jurgen.blog.dao.RoleDao;
import com.jurgen.blog.dao.UserDao;
import com.jurgen.blog.domain.Comment;
import com.jurgen.blog.domain.Post;
import com.jurgen.blog.domain.User;
import com.jurgen.blog.domain.UserRole;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "postService")
@Transactional
public class PostService {

    @Autowired
    private PostDao postDao;

    @Autowired
    private CommentDao commentDao;

    private final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public PostService() {
        LOG.info("postService created");
    }

    public List<Post> getRecentPosts(int amount) {
        return postDao.getRecentPosts(amount);
    }

    public void addComment(String content, User author, Post post) {
        Comment comment = new Comment();
        comment.setContent(content);
        comment.setAuthor(author);
        comment.setPost(post);
        comment.setCommentDate(new Timestamp(System.currentTimeMillis()));
        commentDao.create(comment);
    }

    public void addPost(String title, String content, User user) {
        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setPostDate(new Date(System.currentTimeMillis()));
        post.setAuthor(user);
        postDao.create(post);
    }

    public List<Post> getUsersPosts(User user) {
        return postDao.getUsersPosts(user);
    }

    public Post getPost(Integer id) {
        return postDao.get(id);
    }

    public PostDao getPostDao() {
        return postDao;
    }

    public void setPostDao(PostDao postDao) {
        this.postDao = postDao;
    }

    public CommentDao getCommentDao() {
        return commentDao;
    }

    public void setCommentDao(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

}
