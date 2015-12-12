package com.jurgen.blog.dao;

import java.io.Serializable;
import java.util.List;
import java.lang.reflect.Type;
import java.lang.reflect.ParameterizedType;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public class GenericDaoImpl<T, PK extends Serializable> implements GenericDao<T, PK> {

    protected final Class<T> type;

    @Autowired
    private SessionFactory sessionFactory;

    public GenericDaoImpl() {
        Type t = getClass().getGenericSuperclass();
        ParameterizedType pt = (ParameterizedType) t;
        this.type = (Class<T>) pt.getActualTypeArguments()[0];
    }

    @Override
    public PK create(T object) {
        PK pk = null;
        try {
            pk = (PK) getCurrentSession().save(object);
        } catch (DataIntegrityViolationException ex) {
            System.out.println(ex.getMessage());
        }
        return pk;
    }

    @Override
    public T get(PK id) {
        T result = (T) getCurrentSession().createCriteria(type).add(Restrictions.idEq(id)).uniqueResult();
        return result;
    }

    @Override
    public void update(T object) {
        getCurrentSession().update(object);
    }

    @Override
    public void delete(T object) {
        if (object != null) {
            getCurrentSession().delete(object);
        }
    }

    @Override
    public List<T> getAll() {
        List<T> results = getCurrentSession().createCriteria(type).list();
        return results;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

}
