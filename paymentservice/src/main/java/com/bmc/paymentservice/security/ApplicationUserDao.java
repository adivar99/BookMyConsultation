package com.bmc.doctorservice.security;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String username);
}
