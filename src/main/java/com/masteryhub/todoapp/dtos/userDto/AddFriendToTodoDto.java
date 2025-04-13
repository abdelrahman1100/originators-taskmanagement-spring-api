package com.masteryhub.todoapp.dtos.userDto;

import com.masteryhub.todoapp.models.todoModel.Permission;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AddFriendToTodoDto {
  private String username;
  private Permission permission;
}
