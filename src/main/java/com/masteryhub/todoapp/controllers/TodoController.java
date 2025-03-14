package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.DueDateDto;
import com.masteryhub.todoapp.dto.RequestTodoDto;
import com.masteryhub.todoapp.dto.ResponseTodoDto;
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
  public ResponseEntity<ResponseTodoDto> createTodo(
      @RequestHeader("Authorization") String token, @RequestBody RequestTodoDto requestTodoDto) {
    return todoService.createTodo(token, requestTodoDto);
  }

  @GetMapping("/get-todo")
  public ResponseEntity<ResponseTodoDto> getTodo(
      @RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
    return todoService.getTodo(token, id);
  }

  @GetMapping("/get-todos")
  public ResponseEntity<List<ResponseTodoDto>> getTodos(
      @RequestHeader("Authorization") String token,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return todoService.getTodos(token, page, size);
  }

  @GetMapping("/get-todos-by-status")
  public ResponseEntity<List<ResponseTodoDto>> getTodosByStatus(
      @RequestHeader("Authorization") String token,
      @RequestParam("status") String status,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return todoService.getTodosByStatus(token, status, page, size);
  }

  @PutMapping("/edit-todo")
  public ResponseEntity<?> editTodo(
      @RequestHeader("Authorization") String token, @RequestBody RequestTodoDto requestTodoDto) {
    return todoService.editTodo(token, requestTodoDto);
  }

  @PutMapping("/edit-many-todos")
  public ResponseEntity<?> editManyTodos(
      @RequestHeader("Authorization") String token,
      @RequestBody List<RequestTodoDto> requestTodoDtoList) {
    return todoService.editManyTodos(token, requestTodoDtoList);
  }

  @DeleteMapping("/hard-delete-todo")
  public ResponseEntity<String> hardDeleteTodo(
      @RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
    return todoService.hardDeleteTodo(token, id);
  }

  @DeleteMapping("/hard-delete-many-todos")
  public ResponseEntity<String> hardDeleteManyTodos(
      @RequestHeader("Authorization") String token, @RequestParam("ids") String ids) {
    return todoService.hardDeleteManyTodos(token, ids);
  }

  @DeleteMapping("/hard-delete-all-todos")
  public ResponseEntity<String> hardDeleteAllTodos(@RequestHeader("Authorization") String token) {
    return todoService.hardDeleteAllTodos(token);
  }

  @DeleteMapping("/soft-delete-todo")
  public ResponseEntity<String> softDeleteTodo(
      @RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
    return todoService.softDeleteTodo(token, id);
  }

  @DeleteMapping("/soft-delete-many-todo")
  public ResponseEntity<String> softDeleteManyTodo(
      @RequestHeader("Authorization") String token, @RequestParam("ids") String ids) {
    return todoService.softDeleteManyTodo(token, ids);
  }

  @DeleteMapping("/soft-delete-all-todo")
  public ResponseEntity<String> softDeleteAllTodo(@RequestHeader("Authorization") String token) {
    return todoService.softDeleteAllTodo(token);
  }

  @PatchMapping("/restore-todo")
  public ResponseEntity<ResponseTodoDto> restoreTodo(
      @RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
    return todoService.restoreTodo(token, id);
  }

  @PatchMapping("/restore-many-todo")
  public ResponseEntity<List<ResponseTodoDto>> restoreManyTodo(
      @RequestHeader("Authorization") String token, @RequestParam("ids") String ids) {
    return todoService.restoreManyTodo(token, ids);
  }

  @PatchMapping("/restore-all-todo")
  public ResponseEntity<List<ResponseTodoDto>> restoreAllTodo(
      @RequestHeader("Authorization") String token) {
    return todoService.restoreAllTodo(token);
  }

  @PatchMapping("/set-due-date")
  public ResponseEntity<ResponseTodoDto> setDueDate(
      @RequestHeader("Authorization") String token,
      @RequestParam("id") Long id,
      @RequestBody DueDateDto dueDate) {
    return todoService.setDueDate(token, id, dueDate);
  }
}
