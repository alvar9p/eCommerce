package com.shopme.admin.security;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class ShopmeUserDetails implements UserDetails{

    private User user;

    public ShopmeUserDetails(User user){
        this.user = user;
    }

    // List de roles asignados a un usuario
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Primero obtiene un conjunto de roles
        Set<Role> roles = user.getRoles();

        // Crea una lista para almacenar las autoridades
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();

        // Itera los roles y los agrega a la lista de autoridades
        for(Role role : roles){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }

        // Retorna las autoridades como collection
        return authorities;
    }

    // Retornamos el password del usuario
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.isEnabled();
    }
}
