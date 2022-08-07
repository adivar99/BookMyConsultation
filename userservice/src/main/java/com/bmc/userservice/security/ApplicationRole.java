package com.bmc.userservice.security;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationRole {

    ADMIN(Sets.newHashSet(ApplicationPermission.ADMIN_PERMISSIONS)),
    USER(Sets.newHashSet(ApplicationPermission.USER_PERMISSIONS));



    private Set<ApplicationPermission> permissions;

    ApplicationRole(Set<ApplicationPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationPermission> getPermissions(){
        return this.getPermissions();
    }

    public Set<SimpleGrantedAuthority> getAuthorities(){
        Set<SimpleGrantedAuthority> authorities = permissions.stream().map(
                p-> new SimpleGrantedAuthority(p.name()))
                .collect(Collectors.toSet());
        authorities.add(new SimpleGrantedAuthority("ROLE_"+this.name()));
        return authorities;
    }
}
