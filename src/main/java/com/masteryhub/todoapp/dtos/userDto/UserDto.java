package com.masteryhub.todoapp.dtos.userDto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class UserDto {
  private String username;
  private String email;
}
