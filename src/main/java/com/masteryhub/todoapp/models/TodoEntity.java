package com.masteryhub.todoapp.models;

import com.masteryhub.todoapp.dto.RequestTodoDto;
import java.time.Instant;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "todos")
@Setter
@Getter
@NoArgsConstructor
public class TodoEntity {

  @Id private String id;

  private Long customId;

  private String title;

  private String description;

  private Status status;

  @Field("tags")
  private List<String> tags;

  @CreatedDate
  @Field("created_at")
  private Instant createdAt;

  @LastModifiedDate
  @Field("updated_at")
  private Instant updatedAt;

  @Field("deleted_at")
  private Instant deletedAt;

  @Field("due_date")
  private Instant dueDate;

  @Version private Long __v;

  public TodoEntity(RequestTodoDto requestTodoDto) {
    this.title = requestTodoDto.getTitle();
    this.description = requestTodoDto.getDescription();
    this.status = requestTodoDto.getStatus();
    this.dueDate = requestTodoDto.getDueDate();
    this.tags = requestTodoDto.getTags();
  }
}
