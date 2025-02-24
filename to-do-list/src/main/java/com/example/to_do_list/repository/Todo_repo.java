package com.example.to_do_list.repository;

import com.example.to_do_list.model.Todo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface Todo_repo extends MongoRepository<Todo,String> {

}
