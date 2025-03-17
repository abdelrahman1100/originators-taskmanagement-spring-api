package com.masteryhub.todoapp.dto;

import com.masteryhub.todoapp.models.Permision;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class FriendsForSharedTodoDto {
  private String username;
  private Permision permission;
}
