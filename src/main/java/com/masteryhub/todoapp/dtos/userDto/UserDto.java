package com.masteryhub.todoapp.dtos.userDto;

import com.masteryhub.todoapp.models.settingsModel.Settings;
import com.masteryhub.todoapp.models.userModel.ProfileImage;
import com.masteryhub.todoapp.models.userModel.UserEntity;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {
  private String id;

  private String username;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  private String password;

  private List<String> todos;

  private List<String> friends;

  private Integer tokenVersion;

  private ProfileImage profileImage;

  private Settings settings;

  public UserDto(UserEntity user) {
    this.id = user.getId();
    this.username = user.getUsername();
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.phoneNumber = user.getPhoneNumber();
    this.address = user.getAddress();
    this.password = user.getPassword();
    this.todos = new ArrayList<>();
    this.friends = new ArrayList<>();
    this.tokenVersion = user.getTokenVersion();
    this.profileImage = user.getProfileImage();
    this.settings = user.getSettings();
  }
}
