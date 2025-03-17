package com.masteryhub.todoapp.dto;

import com.masteryhub.todoapp.models.Status;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestTodoDto {

  private Long id;

  private String title;

  private String description;

  private Status status;

  private Instant dueDate;

  private List<String> tags;
}
