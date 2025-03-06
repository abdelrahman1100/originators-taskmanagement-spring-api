package com.masteryhub.todoapp.models;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "blacklisted_tokens")
@TypeAlias("BlacklistedTokens")
public class BlacklistedTokens {

        private String token;

        public BlacklistedTokens(String token) {
            this.token = token;
        }

        public String getToken() {
            return token;
        }
}
