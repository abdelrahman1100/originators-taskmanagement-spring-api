package com.masteryhub.todoapp.dtos.todoDto;

import com.masteryhub.todoapp.models.todoModel.Status;
import com.masteryhub.todoapp.models.todoModel.TodoEntity;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TodoDto {

  private Long customId;

  private String title;

  private String description;

  private Status status;

  private List<String> tags;

  private Instant dueDate;

  public TodoDto(List<TodoEntity> todo) {
    for (TodoEntity todoEntity : todo) {
      this.customId = todoEntity.getCustomId();
      this.title = todoEntity.getTitle();
      this.description = todoEntity.getDescription();
      this.status = todoEntity.getStatus();
      this.tags = todoEntity.getTags();
      this.dueDate = todoEntity.getDueDate();
    }
  }
}
