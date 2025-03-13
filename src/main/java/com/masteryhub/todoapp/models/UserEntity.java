package com.masteryhub.todoapp.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user")
@TypeAlias("User")
@Setter
@Getter
public class UserEntity {

  @Id private String id;

  private String username;

  private String email;

  private String password;

  private List<String> todosIds = new ArrayList<>();

  private Integer __v = 0;

  public List<String> getTodosIds() {
    return Collections.unmodifiableList(todosIds);
  }

  public void setTodosIds(List<String> newTodosIds) {
    this.todosIds = new ArrayList<>(newTodosIds);
  }

  public void addTodoId(String todoId) {
    this.todosIds.add(todoId);
  }

  public void removeTodoId(String todoId) {
    this.todosIds.remove(todoId);
  }

  public void clearTodosIds() {
    this.todosIds.clear();
  }
}
