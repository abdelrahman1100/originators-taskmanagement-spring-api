package com.example.to_do_list.DTO;

public class JWTDto {

    private String token;

    private String id;

    private String username;

    public String getToken() {
        return token;
    }

    public JWTDto(String token, String id, String username) {
        this.token = token;
        this.id = id;
        this.username = username;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
