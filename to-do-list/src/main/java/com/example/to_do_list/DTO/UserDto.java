package com.example.to_do_list.DTO;

import com.example.to_do_list.model.Todo;

import java.util.List;

public class UserDto {

    private String id;

    private String user_name;

    public void setPassword(String password) {
        this.password = password;
    }

    private String password;

    private List<Todo> todolists;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public List<Todo> getTodolists() {
        return todolists;
    }

    public void setTodolists(List<Todo> todolists) {
        this.todolists = todolists;
    }
}
