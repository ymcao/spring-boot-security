package com.mobile2016.security.mapper;
import com.mobile2016.security.model.User;
import com.mobile2016.security.model.UserAuthority;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by caoyamin on 2016/11/22.
 */
@Mapper
public interface UserMapper {

    @Select("SELECT * FROM USER where username = #{username}")
    User findByUsername(@Param("username") String username);

    @Insert("INSERT INTO USER(username, password,email,enabled,avatar,lastPasswordResetDate)" +
            " VALUES(#{username}, #{password}, #{email}, #{enabled}, #{avatar}, #{lastPasswordResetDate})")
    @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
    public long insertUser(User user);

}
