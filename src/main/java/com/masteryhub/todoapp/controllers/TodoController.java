package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.TodoDto;
import com.masteryhub.todoapp.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @GetMapping("/get-todos")
    public ResponseEntity<?> getTodos(@RequestHeader("Authorization") String token, @RequestHeader("page") int page, @RequestHeader("size") int size) {
        return todoService.getTodos(token, page, size);
    }

    @PostMapping("/create-todo")
    public ResponseEntity<?> createTodo(@RequestHeader("Authorization") String token, @RequestBody TodoDto todoDto) {
        return todoService.createTodo(token, todoDto);
    }

    @PutMapping("/edit-todo")
    public ResponseEntity<?> editTodo(@RequestHeader("Authorization") String token, @RequestBody TodoDto todoDto) {
        return todoService.editTodo(token, todoDto);
    }

    @DeleteMapping("/delete-todo")
    public ResponseEntity<?> deleteTodo(@RequestHeader("Authorization") String token, @RequestHeader("id") Long id) {
        return todoService.deleteTodo(token, id);
    }

    @GetMapping("/get-todo")
    public ResponseEntity<?> getTodo(@RequestHeader("Authorization") String token, @RequestHeader("id") Long id) {
        return todoService.getTodo(token, id);
    }

    @DeleteMapping("/delete-all-todos")
    public ResponseEntity<?> deleteAllTodos(@RequestHeader("Authorization") String token) {
        return todoService.deleteAllTodos(token);
    }

    @GetMapping("/get-todos-by-status")
    public ResponseEntity<?> getTodosByStatus(@RequestHeader("Authorization") String token, @RequestHeader("status") String status, @RequestHeader("page") int page, @RequestHeader("size") int size) {
        return todoService.getTodosByStatus(token, status, page, size);
    }

    @DeleteMapping("/delete-many-todos")
    public ResponseEntity<?> deleteManyTodos(@RequestHeader("Authorization") String token, @RequestHeader("ids") String ids) {
        return todoService.deleteManyTodos(token, ids);
    }

    @PutMapping("/edit-many-todos")
    public ResponseEntity<?> editManyTodos(@RequestHeader("Authorization") String token, @RequestBody List<TodoDto> todoDtoList) {
        return todoService.editManyTodos(token, todoDtoList);
    }

}
