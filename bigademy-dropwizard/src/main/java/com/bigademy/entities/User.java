package com.bigademy.entities;

import javax.persistence.*;

/**
 * Created by mshah on 4/25/14.
 */
@Entity
@Table(name = "users")
@NamedQueries({
        @NamedQuery(
                name = "com.bigademy.service.core.User.verifyUser",
                query = "SELECT user FROM User user where email = :email and password= :password"
        ),
        @NamedQuery(
                name = "com.bigademy.service.core.User.verifyUserUsingEmail",
                query = "SELECT user FROM User user where email = :email"
        )
})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private long user_id;

    @Column(name="name", nullable = false)
    private String name;

    @Column(name="email", nullable = false)
    private String email;

    @Column(name="password", nullable = false)
    private String password;

    @Column(name="token", nullable = false)
    private String token;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
