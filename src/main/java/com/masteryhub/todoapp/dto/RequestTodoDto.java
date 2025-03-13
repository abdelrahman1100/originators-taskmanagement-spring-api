package com.masteryhub.todoapp.dto;

import com.masteryhub.todoapp.models.Status;
import com.masteryhub.todoapp.models.TodoEntity;

public class TodoDto {

  private Long id;

  private String title;

  private String description;

  private Status status;

  private String createdAt;

  private String updatedAt;

  public TodoDto() {}

  public TodoDto(TodoEntity todoEntity) {
    this.id = todoEntity.getId();
    this.title = todoEntity.getTitle();
    this.description = todoEntity.getDescription();
    this.status = todoEntity.getStatus();
    this.createdAt = todoEntity.getCreatedAt();
    this.updatedAt = todoEntity.getCreatedAt();
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Status getStatus() {
    return status;
  }

  public void setStatus(Status status) {
    this.status = status;
  }

  public String getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(String createdAt) {
    this.createdAt = createdAt;
  }

  public String getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(String updatedAt) {
    this.updatedAt = updatedAt;
  }
}
