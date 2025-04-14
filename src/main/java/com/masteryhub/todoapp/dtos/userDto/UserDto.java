package com.masteryhub.todoapp.dtos.userDto;

import com.masteryhub.todoapp.models.settingsModel.Settings;
import com.masteryhub.todoapp.models.userModel.ProfileImage;
import com.masteryhub.todoapp.models.userModel.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {

  private String username;

  private String fullName;

  private String email;

  private String phoneNumber;

  private ProfileImage profileImage;

  private Settings settings;

  public UserDto(UserEntity user) {
    this.username = user.getUsername();
    this.fullName = user.getFullName();
    this.email = user.getEmail();
    this.phoneNumber = user.getPhoneNumber();
    this.profileImage = user.getProfileImage();
    this.settings = user.getSettings();
  }
}
