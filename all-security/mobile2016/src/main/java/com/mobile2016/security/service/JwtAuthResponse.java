package com.mobile2016.security.service;

import java.io.Serializable;

/**
 * Created by caoyamin on 2016/11/7.
 */
public class JwtAuthResponse implements Serializable {

    private final String token;

    public JwtAuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return this.token;
    }
}