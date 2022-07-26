package com.bmc.doctorservice.security;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import com.bmc.doctorservice.enums.Role;

@Configuration
@EnableWebSecurity
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    PasswordEncoder encoder;
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.
        authorizeRequests()
        .antMatchers("doctors/*").hasRole(Role.ADMIN.toString())
        .antMatchers("doctors/*").hasRole(Role.USER.toString())
        .anyRequest()
        .authenticated()
        .and()
        .httpBasic();
    }

    @Override
    @Bean
    protected UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                Arrays.asList(
                        User.builder().username("admin-user@abc.com").password(encoder.encode("Admin@123")).roles(Role.ADMIN.toString()).build(),
                        User.builder().username("normal-user@abc.com").password(encoder.encode("Test@123")).roles(Role.USER.toString()).build()
                )
        );
    }
}
