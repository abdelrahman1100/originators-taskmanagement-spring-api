package com.masteryhub.todoapp.repository;

import com.masteryhub.todoapp.models.UserEntity;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<UserEntity, String> {
  Optional<UserEntity> findByUsername(String username);

  Optional<UserEntity> findByEmail(String email);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
