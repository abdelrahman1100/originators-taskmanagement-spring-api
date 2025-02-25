package com.example.to_do_list.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
@TypeAlias("User")
public class User {

    @Id
    private String id;

    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTodolists(List<Todo> todolists) {
        this.todolists = todolists;
    }

    private String password;

    private List<Todo> todolists;

    public String getId() {
        return id;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public List<Todo> getTodolists() {
        return todolists;
    }

    public void setTodolists(Todo todolists) {
        this.todolists.add(todolists);
    }
}
