package com.example.to_do_list.repository;


import com.example.to_do_list.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface User_repo extends MongoRepository<User,Integer> {

}
