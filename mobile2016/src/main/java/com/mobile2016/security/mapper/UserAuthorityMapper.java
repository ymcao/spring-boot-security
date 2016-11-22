package com.mobile2016.security.mapper;

import com.mobile2016.security.model.UserAuthority;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by caoyamin on 2016/11/22.
 */
@Mapper
public interface UserAuthorityMapper {
    @Insert("INSERT INTO USERAUTHORITY(authorityname, userid)" +
            " VALUES(#{authorityname}, #{userid})")
    boolean insertUserAuthority(@Param("authorityname") String authorityname, @Param("userid")long userid);

    @Select("SELECT * FROM USERAUTHORITY where userid = #{userid}")
    @Results({
            @Result(property = "authorityname",  column = "authorityname")
    })
    List<UserAuthority> findAuthoritiesByID(@Param("userid") long userid);

}
