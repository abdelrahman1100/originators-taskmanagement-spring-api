package com.masteryhub.todoapp.models;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class UserDetailsImpl implements UserDetails {

  private String id;
  private String username;
  private String password;

  public UserDetailsImpl(String id, String username, String password) {
    this.id = id;
    this.username = username;
    this.password = password;
  }

  public String getId() {
    return id;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of();
  }

  @Override
  public String getPassword() {
    return password;
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

  public static UserDetailsImpl build(UserEntity userEntity) {
    return new UserDetailsImpl(
        userEntity.getId(), userEntity.getUsername(), userEntity.getPassword());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    UserDetailsImpl that = (UserDetailsImpl) obj;
    return this.id.equals(that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id); // Ensures consistency with equals()
  }
}
