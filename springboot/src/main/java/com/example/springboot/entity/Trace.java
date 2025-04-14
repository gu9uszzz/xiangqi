package com.example.springboot.entity;

import lombok.Data;

@Data
public class Trace {
    private Integer id;
    private String date;
    private double amount;
    private String type;
    /*{
        demand_deposit,//活期存款
        time_deposit,//定期存款
        current_withdrawal,//活期取款
        time_withdrawal,//定期取款
    }*/

    private double money;

    private String token;
}
