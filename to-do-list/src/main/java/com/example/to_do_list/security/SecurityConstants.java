package com.example.to_do_list.security;

import io.github.cdimascio.dotenv.Dotenv;

public class SecurityConstants {
    public static final long JWT_EXPIRATION;

    static {
        Dotenv dotenv = Dotenv.load();
        JWT_EXPIRATION = Long.parseLong(dotenv.get("JWT_EXPIRATION"));
    }
}