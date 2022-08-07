package com.bmc.appointmentservice.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Repository
public class ApplicationUserDaoImpl implements ApplicationUserDao{

    @Override
    public ApplicationUser loadUserByUsername(String username) {
        List<ApplicationUser> users = loadAllUsers();
        for (ApplicationUser user :
                users) {
            if (Objects.equals(username, user.getUsername())) {
                return user;
            }
        }
        return null;
    }

    private List<ApplicationUser> loadAllUsers() {
        return Arrays.asList(
                ApplicationUser
                        .builder()
                        .username("admin-user@abc.com")
                        .password("Admin@123")
                        .authorities(ApplicationRole.ADMIN.getAuthorities())
                        .build(),
                ApplicationUser
                        .builder()
                        .username("normal-user@abc.com")
                        .password("Test@123")
                        .authorities(ApplicationRole.USER.getAuthorities())
                        .build()
        );
    }
}
