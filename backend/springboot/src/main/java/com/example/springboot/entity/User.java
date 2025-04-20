package com.example.springboot.entity;

import lombok.Data;

@Data
public class User {

    private Integer id;

    private String username;//用户名

    private String password;//密码

    private String email;//电子邮箱

    private String avatar;//头像

    private String date;//注册日期

    private String lastlogin;//上次登录

    private String address;//手机号

    private String sign;//个性签名


    private String code;

}
