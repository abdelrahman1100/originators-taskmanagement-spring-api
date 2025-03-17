package com.masteryhub.todoapp.repository;

import com.masteryhub.todoapp.models.SharedTodosEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SharedTodoRepository extends MongoRepository<SharedTodosEntity, String> {

  Optional<SharedTodosEntity> findByTodoAndFriend(String id, String id1);

  List<SharedTodosEntity> findByFriend(String id);

  List<SharedTodosEntity> findByTodo(String id);
}
