package com.masteryhub.todoapp.security;

import com.masteryhub.todoapp.models.UserEntity;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
public class UserDetailsImpl implements UserDetails {

  private final String id;
  private final String username;
  private final String email;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;
  private final Integer __v;

  public UserDetailsImpl(
      String id,
      String username,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities,
      Integer __v) {
    this.id = id;
    this.username = username;
    this.email = email;
    this.password = password;
    this.authorities = Collections.unmodifiableList(new ArrayList<>(authorities));
    this.__v = __v;
  }

  public static UserDetailsImpl build(UserEntity user) {
    return new UserDetailsImpl(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        user.getPassword(),
        Collections.emptySet(),
        user.get__v());
  }

  public Collection<? extends GrantedAuthority> getAuthorities() {
    return Collections.unmodifiableCollection(authorities);
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
}
