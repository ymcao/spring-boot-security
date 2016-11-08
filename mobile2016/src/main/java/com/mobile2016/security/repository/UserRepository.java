package com.mobile2016.security.repository;

import com.mobile2016.security.model.User;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by caoyamin on 2016/11/8.
 */

public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
}
