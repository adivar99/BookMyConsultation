package com.bmc.paymentservice.security;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String username);
}
