package com.mobile2016.security.service;

import com.mobile2016.security.model.User;
import com.mobile2016.security.JwtUserFactory;
import com.mobile2016.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Created by caoyamin on 2016/11/7.
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    //private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            return JwtUserFactory.create(user);
        }
    }

    public int save(User user){

        User u=userRepository.findByUsername(user.getUsername());
        if(u==null){
            userRepository.save(user);
            return 0;
        }
        return -1;

    }
}