package com.example.to_do_list.DTO;

import com.example.to_do_list.model.Todo;
import com.mongodb.lang.NonNull;

import java.util.List;

public class UserDto {


    private String id;

    private String username;

    private String password;

    public List<Todo> getTodolists() {
        return todolists;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private List<Todo> todolists;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setTodolists(List<Todo> todolists) {
        this.todolists = todolists;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public List<Todo> getTodoLists() {
        return todolists;
    }

}
