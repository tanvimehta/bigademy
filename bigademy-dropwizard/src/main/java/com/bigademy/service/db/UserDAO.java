package com.bigademy.service.db;


import com.bigademy.entities.User;
import com.google.common.base.Optional;
import com.yammer.dropwizard.hibernate.AbstractDAO;
import org.hibernate.SessionFactory;
import org.hibernate.internal.QueryImpl;

/**
 * Created by mshah on 4/25/14.
 */
public class UserDAO extends AbstractDAO<User> {

    public UserDAO(SessionFactory factory) {
        super(factory);
    }

    public User getUserByEmail(String email, String password) {
        return uniqueResult(namedQuery("com.bigademy.service.core.User.verifyUser").setString("email", email).setString("password", password));
    }

    public boolean verifyUserExists(String email) {
        return getUserByEmail(email) != null;
    }

    public User getUserByEmail(String email) {
        return uniqueResult(namedQuery("com.bigademy.service.core.User.verifyUserUsingEmail").setString("email", email));
    }

    public User persistUser(User user) {
        return this.persist(user);
    }
}
