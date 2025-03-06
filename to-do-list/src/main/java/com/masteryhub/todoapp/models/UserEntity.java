package com.masteryhub.todoapp.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "user")
@TypeAlias("User")
public class UserEntity {

    @Id
    private String id;

    private String username;

    private String email;

    private String password;

    private List<TodoEntity> todolist;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<TodoEntity> getTodolist() {
        return todolist;
    }

    public void setTodolist(List<TodoEntity> todolist) {
        this.todolist = todolist;
    }

}
