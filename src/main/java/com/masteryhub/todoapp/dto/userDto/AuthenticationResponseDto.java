package com.masteryhub.todoapp.dto.userDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationResponseDto {
  private String verificationToken;
  private UserDto user;
}
