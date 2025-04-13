package com.masteryhub.todoapp.dtos.userDto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginDto {
  private String email;
  private String password;
}
