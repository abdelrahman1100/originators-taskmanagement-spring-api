package com.masteryhub.todoapp.dto;

import com.masteryhub.todoapp.models.Status;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RequestTodoDto {

  private Long customId;

  private String title;

  private String description;

  private Status status;
}
