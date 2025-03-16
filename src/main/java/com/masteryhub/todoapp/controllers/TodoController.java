package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.*;
import com.masteryhub.todoapp.service.TodoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

  @Autowired TodoService todoService;

  @PostMapping("/create-todo")
  public ResponseEntity<ResponseTodoDto> createTodo(@RequestBody RequestTodoDto requestTodoDto) {
    return todoService.createTodo(requestTodoDto);
  }

  @GetMapping("/get-todo")
  public ResponseEntity<ResponseTodoDto> getTodo(@RequestParam("id") Long id) {
    return todoService.getTodo(id);
  }

  @GetMapping("/get-todos")
  public ResponseEntity<List<ResponseTodoDto>> getTodos(
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return todoService.getTodos(page, size);
  }

  @GetMapping("/filter-todos")
  public ResponseEntity<List<ResponseTodoDto>> filterTodos(
      @RequestBody RequestTodoDto requestTodoDto,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return todoService.filterTodos(requestTodoDto, page, size);
  }

  @PutMapping("/edit-todo")
  public ResponseEntity<ResponseTodoDto> editTodo(
      @RequestBody RequestTodoDto requestTodoDto, @RequestParam("id") Long id) {
    return todoService.editTodo(requestTodoDto, id);
  }

  @PutMapping("/edit-many-todos")
  public ResponseEntity<List<ResponseTodoDto>> editManyTodos(
      @RequestBody TodoRequestDto todoRequestDto) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.editManyTodos(requestTodoDtoList);
  }

  @DeleteMapping("/soft-delete-todo")
  public ResponseEntity<MessageDto> softDeleteTodo(@RequestParam("id") Long id) {
    return todoService.softDeleteTodo(id);
  }

  @DeleteMapping("/soft-delete-many-todo")
  public ResponseEntity<MessageDto> softDeleteManyTodo(@RequestBody TodoRequestDto todoRequestDto) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.softDeleteManyTodo(requestTodoDtoList);
  }

  @DeleteMapping("/soft-delete-all-todo")
  public ResponseEntity<MessageDto> softDeleteAllTodo() {
    return todoService.softDeleteAllTodo();
  }

  @PatchMapping("/restore-todo")
  public ResponseEntity<MessageDto> restoreTodo(@RequestParam("id") Long id) {
    return todoService.restoreTodo(id);
  }

  @PatchMapping("/restore-many-todo")
  public ResponseEntity<MessageDto> restoreManyTodo(@RequestBody TodoRequestDto todoRequestDto) {
    List<RequestTodoDto> requestTodoDtoList = todoRequestDto.getTodos();
    return todoService.restoreManyTodo(requestTodoDtoList);
  }

  @PatchMapping("/restore-all-todo")
  public ResponseEntity<MessageDto> restoreAllTodo() {
    return todoService.restoreAllTodo();
  }
}
