package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.TodoDto;
import com.masteryhub.todoapp.service.TodoService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

  @Autowired TodoService todoService;

  @GetMapping("/get-todos")
  public ResponseEntity<?> getTodos(
      @RequestHeader("Authorization") String token,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return todoService.getTodos(token, page, size);
  }

  @PostMapping("/create-todo")
  public ResponseEntity<?> createTodo(
      @RequestHeader("Authorization") String token, @RequestBody TodoDto todoDto) {
    return todoService.createTodo(token, todoDto);
  }

  @PutMapping("/edit-todo")
  public ResponseEntity<?> editTodo(
      @RequestHeader("Authorization") String token, @RequestBody TodoDto todoDto) {
    return todoService.editTodo(token, todoDto);
  }

  @DeleteMapping("/hard-delete-todo")
  public ResponseEntity<?> deleteTodo(
      @RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
    return todoService.deleteTodo(token, id);
  }

  @GetMapping("/get-todo")
  public ResponseEntity<?> getTodo(
      @RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
    return todoService.getTodo(token, id);
  }

  @DeleteMapping("/hard-delete-all-todos")
  public ResponseEntity<?> deleteAllTodos(@RequestHeader("Authorization") String token) {
    return todoService.deleteAllTodos(token);
  }

  @GetMapping("/get-todos-by-status")
  public ResponseEntity<?> getTodosByStatus(
      @RequestHeader("Authorization") String token,
      @RequestParam("status") String status,
      @RequestParam(name = "page", defaultValue = "0") int page,
      @RequestParam(name = "size", defaultValue = "10") int size) {
    return todoService.getTodosByStatus(token, status, page, size);
  }

  @DeleteMapping("/hard-delete-many-todos")
  public ResponseEntity<?> deleteManyTodos(
      @RequestHeader("Authorization") String token, @RequestParam("ids") String ids) {
    return todoService.deleteManyTodos(token, ids);
  }

  @PutMapping("/edit-many-todos")
  public ResponseEntity<?> editManyTodos(
      @RequestHeader("Authorization") String token, @RequestBody List<TodoDto> todoDtoList) {
    return todoService.editManyTodos(token, todoDtoList);
  }

  @DeleteMapping("/soft-delete-todo")
  public ResponseEntity<?> softDeleteTodo(
      @RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
    return todoService.softDeleteTodo(token, id);
  }

  @DeleteMapping("/soft-delete-all-todo")
  public ResponseEntity<?> softDeleteAllTodo(@RequestHeader("Authorization") String token) {
    return todoService.softDeleteAllTodo(token);
  }

  @DeleteMapping("/soft-delete-many-todo")
  public ResponseEntity<?> softDeleteManyTodo(
      @RequestHeader("Authorization") String token, @RequestParam("ids") String ids) {
    return todoService.softDeleteManyTodo(token, ids);
  }

  @PutMapping("/restore-todo")
  public ResponseEntity<?> restoreTodo(
      @RequestHeader("Authorization") String token, @RequestParam("id") Long id) {
    return todoService.restoreTodo(token, id);
  }

  @PutMapping("/restore-all-todo")
  public ResponseEntity<?> restoreAllTodo(@RequestHeader("Authorization") String token) {
    return todoService.restoreAllTodo(token);
  }

  @PutMapping("/restore-many-todo")
  public ResponseEntity<?> restoreManyTodo(
      @RequestHeader("Authorization") String token, @RequestParam("ids") String ids) {
    return todoService.restoreManyTodo(token, ids);
  }
}
