package com.masteryhub.todoapp.dto;

import com.masteryhub.todoapp.models.Status;
import com.masteryhub.todoapp.models.TodoEntity;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class ResponseTodoDto {

  private Long customId;

  private String title;

  private String description;

  private Status status;

  private Instant createdAt;

  private Instant updatedAt;

  public static ResponseTodoDto from(TodoEntity todo) {
    return new ResponseTodoDto(
        todo.getCustomId(),
        todo.getTitle(),
        todo.getDescription(),
        todo.getStatus(),
        todo.getCreatedAt(),
        todo.getUpdatedAt());
  }
}
