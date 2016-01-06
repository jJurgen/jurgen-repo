package com.jurgen.blog.dao;

import com.jurgen.blog.domain.Comment;
import org.springframework.stereotype.Component;

@Component("commentDao")
public class CommentDaoImpl extends GenericDaoImpl<Comment, Integer> implements CommentDao{
    
}
