package com.bmc.userservice.security;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String username);
}
