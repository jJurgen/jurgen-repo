package com.jurgen.blog.dao;

import com.jurgen.blog.domain.Post;
import com.jurgen.blog.domain.User;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

@Component("postDao")
public class PostDaoImpl extends GenericDaoImpl<Post, Integer> implements PostDao {

    @Override
    public List<Post> getUsersPosts(User user) {
        List<Post> posts = getCurrentSession().createCriteria(type).add(Restrictions.eq("author", user)).list();
        return posts;
    }

    @Override
    public List<Post> getRecentPosts(int amount) {
        Criteria c = getCurrentSession().createCriteria(type);
        //c.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
        c.addOrder(Order.desc("postDate"));
        c.addOrder(Order.asc("content"));
        c.setMaxResults(amount);
        return c.list();
    }

}
