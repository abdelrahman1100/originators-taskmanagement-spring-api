package com.example.to_do_list.service;

import com.example.to_do_list.DTO.UserDto;
import com.example.to_do_list.model.Todo;
import com.example.to_do_list.model.User;
import com.example.to_do_list.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepo userRepo;

}
