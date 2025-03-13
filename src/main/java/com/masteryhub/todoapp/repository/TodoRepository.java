package com.masteryhub.todoapp.repository;

import com.masteryhub.todoapp.models.Status;
import com.masteryhub.todoapp.models.TodoEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TodoRepository extends MongoRepository<TodoEntity, String> {
  List<TodoEntity> findByCustomId(Long customId);

  List<TodoEntity> findAllByCustomId(List<Long> customIds);

  Page<TodoEntity> findAllByIdIn(List<String> ids, Pageable pageable);

  Page<TodoEntity> findByIdInAndStatus(List<String> ids, Status status, Pageable pageable);
}
