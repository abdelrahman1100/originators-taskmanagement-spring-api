package com.example.to_do_list.controllers;

import com.example.to_do_list.dto.TodoDto;
import com.example.to_do_list.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todo")
public class TodoController {

    @Autowired
    TodoService todoService;

    @GetMapping("/get-todos")
    public ResponseEntity<?> getTodos(@RequestHeader("Authorization") String token) {
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
    public ResponseEntity<?> deleteTodo(@RequestHeader("Authorization") String token, @RequestHeader Long id) {
        token = token.substring(7);
        return todoService.deleteTodo(token, id);
    }

}
