package com.masteryhub.todoapp.dto;

import com.masteryhub.todoapp.models.Permission;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddFriendDto {
  private String username;
  private Permission permission;
}
