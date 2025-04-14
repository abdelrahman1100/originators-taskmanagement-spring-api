package com.masteryhub.todoapp.dtos.todoDto;

import java.util.List;
import lombok.Data;

@Data
public class RequestTodoDtoList {
  List<RequestTodoDto> todos;
}
