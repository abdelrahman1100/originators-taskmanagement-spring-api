package com.example.to_do_list.controller;

import com.example.to_do_list.DTO.TodoDto;
import com.example.to_do_list.service.TodoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @Autowired
    TodoService todoService;

    //get list

    //get all

    //create
    @PostMapping("/create")
    public void create(@RequestBody TodoDto dto_todo){
        todoService.create(dto_todo);
    }
    //delete

    //update

}
