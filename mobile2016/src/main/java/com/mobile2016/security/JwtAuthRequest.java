package com.mobile2016.security;

import java.io.Serializable;

/**
 * Created by caoyamin on 2016/11/7.
 */
public class JwtAuthRequest implements Serializable {

    private String username;
    private String password;

    public JwtAuthRequest() {
        super();
    }

    public JwtAuthRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}