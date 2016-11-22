package com.mobile2016.security.service;

import com.mobile2016.security.mapper.UserAuthorityMapper;
import com.mobile2016.security.mapper.UserMapper;
import com.mobile2016.security.model.Authority;
import com.mobile2016.security.model.User;
import com.mobile2016.security.JwtUserFactory;
import com.mobile2016.security.model.UserAuthority;
import com.sun.javafx.scene.control.skin.VirtualFlow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by caoyamin on 2016/11/7.
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    //private static final Logger logger = LoggerFactory.getLogger(JwtUserDetailsServiceImpl.class);
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserAuthorityMapper authorityMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        } else {
            List<UserAuthority> authorityList=authorityMapper.findAuthoritiesByID(user.getId());
            if(authorityList!=null) {
                List<Authority> authorities = new ArrayList<>();
                for (int i = 0; i < authorityList.size(); i++) {
                    Authority authority = new Authority();
                    authority.setName(authorityList.get(i).authorityname);
                }
                user.setAuthorities(authorities);
            }
            return JwtUserFactory.create(user);
        }
    }
    public boolean save(User user){

        User u=userMapper.findByUsername(user.getUsername());
        if(u==null){
            userMapper.insertUser(user);
            if(user.getAuthorities()!=null&&user.getAuthorities().size()>0){
                for(Authority authority:user.getAuthorities()){
                    authorityMapper.insertUserAuthority(authority.getName(),user.getId());
                }
            }
            return true;
        }else{
            throw new UsernameNotFoundException(String.format(" User has exist'%s'.", user.getUsername()));
        }
    }
}