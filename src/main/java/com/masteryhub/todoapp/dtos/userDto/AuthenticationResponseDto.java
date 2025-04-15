package com.masteryhub.todoapp.dtos.userDto;

import com.masteryhub.todoapp.dtos.todoDto.TodoDto;
import com.masteryhub.todoapp.models.todoModel.TodoEntity;
import com.masteryhub.todoapp.models.userModel.UserEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class AuthenticationResponseDto {
  private String verificationToken;
  private UserDto user;
  private TodoDto todo;

  public AuthenticationResponseDto(
      String verificationToken, UserEntity user, List<TodoEntity> todo) {
    this.verificationToken = verificationToken;
    this.user = new UserDto(user);
    this.todo = new TodoDto(todo);
  }
}
