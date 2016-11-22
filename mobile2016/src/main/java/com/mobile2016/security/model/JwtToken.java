package com.mobile2016.security.model;

import java.io.Serializable;

/**
 * Created by caoyamin on 2016/11/7.
 */
public class JwtToken implements Serializable {

    private final String token;

    public JwtToken(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}