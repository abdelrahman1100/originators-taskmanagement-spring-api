package com.example.to_do_list.controllers;

import com.example.to_do_list.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class TodoController {

    @Autowired
    TodoService todoService;

//    @GetMapping("/get-list")
//    public List<Todo> getList(@RequestParam String id) {
//    }
//
//    @PostMapping("/create-list")
//    public void createList(@RequestParam String id, @RequestBody Todo todoDto) {
//    }
//
//    @DeleteMapping("/delete-list")
//    public void deleteList(@RequestBody UserDto userDto) {
//    }
//
//    @PutMapping("/update-list")
//    public void updateList(@RequestBody UserDto userDto) {
//    }
}
