package com.masteryhub.todoapp.models.notificationModel;

import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import nonapi.io.github.classgraph.json.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document(collection = "notifications")
@Setter
@Getter
public class NotificationModel {

  @Id private String id;

  private String recipient;

  private NotificationType type;

  private String message;

  private boolean isRead = false;

  private String sender;

  @CreatedDate
  @Field("created_at")
  private Instant createdAt;

  @LastModifiedDate
  @Field("updated_at")
  private Instant updatedAt;
}
