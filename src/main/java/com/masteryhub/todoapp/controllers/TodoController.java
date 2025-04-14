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
@RequestMapping("/todos")
public class TodoController {

  @Autowired TodoService todoService;

  @PostMapping("/")
  public ResponseEntity<ResponseTodoDto> createTodo(@RequestBody RequestTodoDto requestTodoDto) {
    return todoService.createTodo(requestTodoDto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ResponseTodoDto> getTodo(@PathVariable("id") Long id) {
    return todoService.getTodo(id);
  }

  @GetMapping("/")
  public ResponseEntity<List<ResponseTodoDto>> getTodos(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int limit) {
    return todoService.getTodos(page, limit);
  }

  @GetMapping("/filter")
  public ResponseEntity<List<ResponseTodoDto>> filterTodos(
      @RequestBody RequestTodoDto requestTodoDto,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return todoService.filterTodos(requestTodoDto, page, size);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ResponseTodoDto> editTodo(
      @RequestBody RequestTodoDto requestTodoDto, @PathVariable("id") Long id) {
    return todoService.editTodo(requestTodoDto, id);
  }

  @PutMapping("/bulk")
  public ResponseEntity<List<ResponseTodoDto>> editManyTodos(
      @RequestBody RequestTodoDtoList todoRequestDto) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.editManyTodos(requestTodoDtoList);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<MessageDto> softDeleteTodo(@PathVariable("id") Long id) {
    return todoService.softDeleteTodo(id);
  }

  @DeleteMapping("/bulk")
  public ResponseEntity<MessageDto> softDeleteManyTodo(
      @RequestBody RequestTodoDtoList todoRequestDto) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.softDeleteManyTodo(requestTodoDtoList);
  }

  @DeleteMapping("/all")
  public ResponseEntity<MessageDto> softDeleteAllTodo() {
    return todoService.softDeleteAllTodo();
  }

  @PatchMapping("/{id}/restore")
  public ResponseEntity<MessageDto> restoreTodo(@PathVariable("id") Long id) {
    return todoService.restoreTodo(id);
  }

  @PatchMapping("/bulk")
  public ResponseEntity<MessageDto> restoreManyTodo(
      @RequestBody RequestTodoDtoList todoRequestDto) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.restoreManyTodo(requestTodoDtoList);
  }

  @PatchMapping("/all")
  public ResponseEntity<MessageDto> restoreAllTodo() {
    return todoService.restoreAllTodo();
  }
}
