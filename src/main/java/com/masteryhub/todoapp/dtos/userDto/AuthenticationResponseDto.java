package com.masteryhub.todoapp.dtos.userDto;

import com.masteryhub.todoapp.models.userModel.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationResponseDto {
  private String verificationToken;
  private UserDto user;

  public AuthenticationResponseDto(String verificationToken, UserEntity user) {
    this.verificationToken = verificationToken;
    this.user = new UserDto(user);
  }
}
