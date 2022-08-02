package com.bmc.ratingservice.security;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String username);
}
