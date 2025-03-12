package com.masteryhub.todoapp.repository;

import com.masteryhub.todoapp.models.BlacklistedTokens;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<BlacklistedTokens, String> {
  Boolean existsByToken(String token);
}
