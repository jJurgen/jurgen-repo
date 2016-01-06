package com.jurgen.blog.dao;

import com.jurgen.blog.domain.UserRole;
import java.io.Serializable;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Component;

@Component("roleDao")
public class RoleDaoImpl extends GenericDaoImpl<UserRole, Integer> implements RoleDao {

    @Override
    public UserRole getRoleByName(String roleName) {
        UserRole role = (UserRole) getCurrentSession().createCriteria(type).add(Restrictions.eq("role", roleName)).uniqueResult();
        return role;
    }

}
