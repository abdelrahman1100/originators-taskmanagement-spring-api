package com.masteryhub.todoapp.dto;

import java.util.List;
import lombok.Data;

@Data
public class TodoRequestDto {
  List<RequestTodoDto> todos;
}
