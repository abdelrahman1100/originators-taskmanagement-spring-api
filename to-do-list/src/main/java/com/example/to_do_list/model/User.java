package com.example.to_do_list.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document
@TypeAlias("User")
public class User {

    @Field
    private String id;

    private String user_name;

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
