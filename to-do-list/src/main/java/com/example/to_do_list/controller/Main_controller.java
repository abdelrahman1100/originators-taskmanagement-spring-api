package com.example.to_do_list.controller;

import com.example.to_do_list.model.DTO_Todo;
import com.example.to_do_list.model.Todo;
import com.example.to_do_list.repository.Todo_repo;
import com.example.to_do_list.repository.User_repo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Main_controller {

    @Autowired
    User_repo userRepo;

    @Autowired
    Todo_repo todoRepo;

    //get list

    //get all

    //create
    @PostMapping("/create")
    public void create(@RequestBody DTO_Todo dto_todo){
        Todo todo = new Todo();
        todo.setTask(dto_todo.getTask());
        todo.setDescription(dto_todo.getDescription());
        todo.setId(dto_todo.getId());
        todoRepo.save(todo);
    }

    //delete

    //update

}
