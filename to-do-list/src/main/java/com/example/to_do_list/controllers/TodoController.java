package com.example.to_do_list.controllers;

import com.example.to_do_list.dto.TodoDto;
import com.example.to_do_list.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @GetMapping("/get-todos")
    public ResponseEntity<?> getTodos(@RequestHeader("Authorization") String token) {
        // TODO: remove any business code to services and make controller plain and simple 
        // pass token to service and return the response
        token = token.substring(7);
        return todoService.getTodos(token);
    }

    @PostMapping("/create-todo")
    public ResponseEntity<?> createTodo(@RequestHeader("Authorization") String token, @RequestBody TodoDto todoDto) {
        token = token.substring(7);
        return todoService.createTodo(token, todoDto);
    }

    @PutMapping("/edit-todo")
    public ResponseEntity<?> editTodo(@RequestHeader("Authorization") String token, @RequestBody TodoDto todoDto) {
        token = token.substring(7);
        return todoService.editTodo(token, todoDto);
    }

    @DeleteMapping("/delete-todo")
    public ResponseEntity<?> deleteTodo(@RequestHeader("Authorization") String token, @RequestHeader("id") Long id) {
        token = token.substring(7);
        return todoService.deleteTodo(token, id);
    }

    @GetMapping("/get-todo")
    public ResponseEntity<?> getTodo(@RequestHeader("Authorization") String token, @RequestHeader("id") Long id) {
        token = token.substring(7);
        return todoService.getTodo(token, id);
    }

    @DeleteMapping("/delete-all-todos")
    public ResponseEntity<?> deleteAllTodos(@RequestHeader("Authorization") String token) {
        token = token.substring(7);
        return todoService.deleteAllTodos(token);
    }

    @GetMapping("/get-todos-by-status")
    public ResponseEntity<?> getTodosByStatus(@RequestHeader("Authorization") String token, @RequestHeader("status") String status) {
        token = token.substring(7);
        return todoService.getTodosByStatus(token, status);
    }

    @DeleteMapping("/delete-many-todos")
    public ResponseEntity<?> deleteManyTodos(@RequestHeader("Authorization") String token, @RequestHeader("ids") String ids) {
        token = token.substring(7);
        return todoService.deleteManyTodos(token, ids);
    }

    @PutMapping("/edit-many-todos")
    public ResponseEntity<?> editManyTodos(@RequestHeader("Authorization") String token, @RequestBody List<TodoDto> todoDtoList) {
        token = token.substring(7);
        return todoService.editManyTodos(token, todoDtoList);
    }

}
