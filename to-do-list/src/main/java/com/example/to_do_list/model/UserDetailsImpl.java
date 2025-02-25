package com.example.to_do_list.model;

import com.example.to_do_list.DTO.UserDto;
import org.springframework.data.annotation.Id;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

    private String id;

    private String username;

    private String password;

    private List<Todo> todolists;

    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(String id, String username, String password, List<Todo> todolists) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.todolists = todolists;
    }

    public UserDetailsImpl(UserDto request) {
    }

    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
        return true;
    }

    public List<Todo> getTodolists() {
        return todolists;
    }

    public void setTodolists(Todo todolists) {
        this.todolists.add(todolists);
    }

    public static UserDetailsImpl build(User user) {
        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getTodolists());
    }

    public static UserDetailsImpl buildFromDto(UserDto userdto) {
        return new UserDetailsImpl(
                userdto.getId(),
                userdto.getUsername(),
                userdto.getPassword(),
                userdto.getTodolists());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

}
