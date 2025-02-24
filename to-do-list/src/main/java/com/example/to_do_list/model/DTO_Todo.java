package com.example.to_do_list.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

public class DTO_Todo {

    private String id;

    private String task;

    private String description;

    public String getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public String getDescription() {
        return description;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
