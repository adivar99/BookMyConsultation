package com.example.securitydemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public class ApplicationUserDaoImpl implements ApplicationUserDao{

    @Autowired
    PasswordEncoder encoder;

    @Override
    public ApplicationUser loadUserByUsername(String username) {
        return loadAllUsers()
                .stream()
                .filter(u->u.getUsername().equalsIgnoreCase(username))
                .findFirst().orElseThrow(()-> new UsernameNotFoundException(username));
    }

    private List<ApplicationUser> loadAllUsers(){
        return Arrays.asList(
                ApplicationUser
                        .builder()
                        .username("admin-user@abc.com")
                        .password(encoder.encode("Admin@123"))
                        .authorities(ApplicationRole.STUDENT.getAuthorities())
                        .build(),
                ApplicationUser
                        .builder()
                        .username("normal-user@abc.com")
                        .password(encoder.encode("Test@123"))
                        .authorities(ApplicationRole.NEW_ADMIN.getAuthorities())
                        .build()
        );
    }
}
