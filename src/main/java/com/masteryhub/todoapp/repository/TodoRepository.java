package com.masteryhub.todoapp.repository;

import com.masteryhub.todoapp.models.Status;
import com.masteryhub.todoapp.models.TodoEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TodoRepository extends MongoRepository<TodoEntity, String> {
  List<TodoEntity> findByCustomId(Long customId);

  // TODO: remove In from all method names
  List<TodoEntity> findAllByCustomIdIn(List<Long> customIds);

  Page<TodoEntity> findAllByIdIn(List<String> ids, Pageable pageable);

  Page<TodoEntity> findByIdInAndStatus(List<String> ids, Status status, Pageable pageable);

  Page<TodoEntity> findAllByIdInAndDeletedAtIsNull(List<String> ids, Pageable pageable);

  Page<TodoEntity> findByIdInAndStatusAndDeletedAtIsNull(
      List<String> ids, Status status, Pageable pageable);

  Page<TodoEntity> findByTitleContainingIgnoreCaseAndStatusAndTagsInAndDeletedAtIsNull(
      List<String> ids, String title, Status status, List<String> tags, Pageable pageable);

  Page<TodoEntity> findByUserIdAndTitleContainingIgnoreCaseAndStatusAndTagsInAndDeletedAtIsNull(
      String id, String title, Status status, List<String> tags, Pageable pageable);

  Page<TodoEntity> findByUserIdAndTitleContainingIgnoreCaseAndDeletedAtIsNull(
      String id, String title, Pageable pageable);

  Page<TodoEntity> findByUserIdAndTitleContainingIgnoreCaseAndTagsInAndDeletedAtIsNull(
      String id, String title, List<String> tags, Pageable pageable);

  Page<TodoEntity> findByUserIdAndTitleContainingIgnoreCaseAndStatusAndDeletedAtIsNull(
      String id, String title, Status status, Pageable pageable);

  Optional<TodoEntity> findByUserIdAndCustomId(String userId, Long customId);

  Optional<TodoEntity> findByUserIdAndCustomIdAndDeletedAtIsNull(String id, Long id1);

  List<TodoEntity> findByUserIdAndCustomIdIn(String id, List<Long> requestedTodoIds);

  List<TodoEntity> findByUserIdAndCustomIdInAndDeletedAtIsNull(String id, List<Long> idList);

  Optional<TodoEntity> findByUserIdAndCustomIdAndDeletedAtIsNotNull(String id, Long id1);

  List<TodoEntity> findByUserIdAndCustomIdInAndDeletedAtIsNotNull(String id, List<Long> idList);
}
