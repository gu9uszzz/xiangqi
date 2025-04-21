package com.example.springboot.mapper;

import com.example.springboot.entity.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface UserMapper {

    @Insert("insert into `user` (username, password, email, avatar, date, lastlogin, address, sign) " +
            "values (#{username}, #{password}, #{email}, #{avatar}, #{date}, #{lastlogin}, #{address}, #{sign})")
    int insert(User user);


    @Delete("delete from `user` where id = #{id}")
    void delete(Integer id);


    @Select("select * from `user`")
    List<User> selectAll();

    @Select("select * from `user` where id = #{id}")
    User selectByUserId(Integer id);

    @Select("select * from `user` where username = #{username}")
    User selectByUsername(String username);

    @Select("select * from `user` where email = #{email}")
    User selectByEmail(String email);


    @Update("update `user` set username = #{username}, email = #{email}, avatar = #{avatar}, " +
            "date = #{date}, lastlogin = #{lastlogin}, address = #{address}, sign = #{sign} where username = #{username}")
    void update(User user);

    @Update("update `user` set password = #{password} where username = #{username}")
    void updatepass(User user);
}
