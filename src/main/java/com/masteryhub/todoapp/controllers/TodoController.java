package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dtos.messageDto.MessageDto;
import com.masteryhub.todoapp.dtos.todoDto.RequestTodoDto;
import com.masteryhub.todoapp.dtos.todoDto.RequestTodoDtoList;
import com.masteryhub.todoapp.dtos.todoDto.ResponseTodoDto;
import com.masteryhub.todoapp.service.TodoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

  @Autowired TodoService todoService;

  @PostMapping("/")
  public ResponseEntity<ResponseTodoDto> createTodo(@RequestBody RequestTodoDto requestTodoDto) {
    return todoService.createTodo(requestTodoDto);
  }

  @GetMapping("/{username}/{id}")
  public ResponseEntity<ResponseTodoDto> getTodo(
      @PathVariable("id") Long id, @PathVariable("username") String username) {
    return todoService.getTodo(id, username);
  }

  @GetMapping("/{username}")
  public ResponseEntity<List<ResponseTodoDto>> getTodos(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int limit,
      @PathVariable("username") String username) {
    return todoService.getTodos(page, limit, username);
  }

  @GetMapping("/{username}/filter")
  public ResponseEntity<List<ResponseTodoDto>> filterTodos(
      @RequestBody RequestTodoDto requestTodoDto,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size,
      @PathVariable("username") String username) {
    return todoService.filterTodos(requestTodoDto, page, size, username);
  }

  @PutMapping("/{username}/{id}")
  public ResponseEntity<ResponseTodoDto> editTodo(
      @RequestBody RequestTodoDto requestTodoDto,
      @PathVariable("id") Long id,
      @PathVariable("username") String username) {
    return todoService.editTodo(requestTodoDto, id, username);
  }

  @PutMapping("/{username}/bulk")
  public ResponseEntity<List<ResponseTodoDto>> editManyTodos(
      @RequestBody RequestTodoDtoList todoRequestDto, @PathVariable("username") String username) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.editManyTodos(requestTodoDtoList, username);
  }

  @DeleteMapping("/{username}/{id}")
  public ResponseEntity<MessageDto> softDeleteTodo(
      @PathVariable("id") Long id, @PathVariable("username") String username) {
    return todoService.softDeleteTodo(id, username);
  }

  @DeleteMapping("/{username}/bulk")
  public ResponseEntity<MessageDto> softDeleteManyTodo(
      @RequestBody RequestTodoDtoList todoRequestDto, @PathVariable("username") String username) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.softDeleteManyTodo(requestTodoDtoList, username);
  }

  @DeleteMapping("/{username}/all")
  public ResponseEntity<MessageDto> softDeleteAllTodo(@PathVariable("username") String username) {
    return todoService.softDeleteAllTodo(username);
  }

  @PatchMapping("/{username}/restore/{id}")
  public ResponseEntity<MessageDto> restoreTodo(
      @PathVariable("id") Long id, @PathVariable("username") String username) {
    return todoService.restoreTodo(id, username);
  }

  @PatchMapping("/{username}/restore/bulk")
  public ResponseEntity<MessageDto> restoreManyTodo(
      @RequestBody RequestTodoDtoList todoRequestDto, @PathVariable("username") String username) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.restoreManyTodo(requestTodoDtoList, username);
  }

  @PatchMapping("/{username}/restore")
  public ResponseEntity<MessageDto> restoreAllTodo(@PathVariable("username") String username) {
    return todoService.restoreAllTodo(username);
  }
}
