package com.example.springboot.entity;

import lombok.Data;

@Data
public class Rank {

    private Integer user_id;

    private Integer nums;//场次

    private Integer wins;//胜场

    private Integer loses;//负场

    private Integer score;//积分
}
