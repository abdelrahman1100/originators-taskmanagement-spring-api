package com.example.to_do_list.service;

import com.example.to_do_list.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class TodoService {

    @Autowired
    UserRepository userRepository;

}
