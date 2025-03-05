package com.example.to_do_list.repository;

import com.example.to_do_list.models.BlacklistedTokens;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TokenRepository extends MongoRepository<BlacklistedTokens, String> {
    Boolean existsByToken(String token);
}
