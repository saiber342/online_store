package com.jm.online_store.config.security.jwt;

import com.jm.online_store.model.JwtUser;
import com.jm.online_store.model.Role;
import com.jm.online_store.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Implementation of Factory Method for class {@link JwtUser}.
 * Made user to userJVT
 */
public final class JwtUserFactory {

    public JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName(),
                user.getPassword(),
                mapToGrantedAuthorities(new HashSet<>(user.getRoles())),
                user.isEnabled()
        );
    }

    private static Set<GrantedAuthority> mapToGrantedAuthorities(Set<Role> userRoles) {
        return userRoles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toSet());
    }
}
