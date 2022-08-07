package com.bmc.appointmentservice.security;

public interface ApplicationUserDao {
    public ApplicationUser loadUserByUsername(String username);
}
