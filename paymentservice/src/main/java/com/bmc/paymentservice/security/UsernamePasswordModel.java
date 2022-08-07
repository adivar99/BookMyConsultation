package com.bmc.paymentservice.security;

import lombok.Data;

@Data
public class UsernamePasswordModel {
    private String username;
    private String password;
}
