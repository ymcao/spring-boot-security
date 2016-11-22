package com.mobile2016.security.controller;

import com.mobile2016.security.JwtAuthRequest;
import com.mobile2016.security.JwtTokenUtil;
import com.mobile2016.security.JwtUser;
import com.mobile2016.security.JwtUserFactory;
import com.mobile2016.security.model.User;
import com.mobile2016.security.model.JwtToken;
import com.mobile2016.security.service.JwtUserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by caoyamin on 2016/11/8.
 */
@RestController
public class AuthRestController  {

    @Value("${jwt.header}")
    private String tokenHeader;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    @RequestMapping(value = "auth/login", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthRequest authenticationRequest, Device device) throws AuthenticationException {

        // Perform the security
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Reload password post-security so we can generate token
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String token = jwtTokenUtil.generateToken(userDetails, device);

        // Return the token
        return ResponseEntity.ok(new JwtToken(token));
    }

    @RequestMapping(value = "refresh", method = RequestMethod.GET)
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);

        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new JwtToken(refreshedToken));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @RequestMapping(value = "auth/regist", method = RequestMethod.POST)
    public JwtUser register(@RequestBody User user) {

        JwtUser u = JwtUserFactory.create(user);
        if(userDetailsService.save(user)){
            return u;
        }
        return null;
    }


    @RequestMapping(value = "user/self", method = RequestMethod.GET)
    public JwtUser getUserSelf(HttpServletRequest request) {
        String token = request.getHeader(tokenHeader);
        String username = jwtTokenUtil.getUsernameFromToken(token);
        JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
        return user;
    }


}