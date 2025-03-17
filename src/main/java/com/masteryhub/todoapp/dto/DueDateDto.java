package com.masteryhub.todoapp.dto;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DueDateDto {
  private Instant dueDate;
}
