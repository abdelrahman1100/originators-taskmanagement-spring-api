package com.masteryhub.todoapp.models;

import com.masteryhub.todoapp.dto.RequestTodoDto;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "todo")
@TypeAlias("Todo")
@Setter
@Getter
@NoArgsConstructor
public class TodoEntity {

  @Id private String id;

  private Long customId;

  private String title;

  private String description;

  private Status status;

  public TodoEntity(RequestTodoDto requestTodoDto) {
    this.title = requestTodoDto.getTitle();
    this.description = requestTodoDto.getDescription();
    this.status = requestTodoDto.getStatus();
  }

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
}
