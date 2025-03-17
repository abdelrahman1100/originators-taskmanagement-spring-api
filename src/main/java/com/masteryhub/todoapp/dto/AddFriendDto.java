package com.masteryhub.todoapp.dto;

import com.masteryhub.todoapp.models.Permision;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddFriendDto {
  private String username;
  private Permision permission;
}
