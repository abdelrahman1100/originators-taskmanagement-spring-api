package com.example.to_do_list.service;

import com.example.to_do_list.DTO.TodoDto;
import com.example.to_do_list.model.Todo;
import com.example.to_do_list.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TodoService {

    @Autowired
    UserRepo userRepo;

    public void create(TodoDto dtoTodo) {

    }

}
