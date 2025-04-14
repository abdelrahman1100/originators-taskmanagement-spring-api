package com.masteryhub.todoapp.dtos.notificationDto;

import com.masteryhub.todoapp.models.notificationModel.NotificationModel;
import com.masteryhub.todoapp.models.notificationModel.NotificationType;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class NotificationDto {
  private String id;

  private String recipient;

  private NotificationType type;

  private String message;

  private boolean IsRead = false;

  private String sender;

  private Instant createdAt;

  private Instant updatedAt;

  public NotificationDto(NotificationModel notificationModel) {
    this.id = notificationModel.getId();
    this.recipient = notificationModel.getRecipient();
    this.type = notificationModel.getType();
    this.message = notificationModel.getMessage();
    this.IsRead = false;
    this.sender = notificationModel.getSender();
    this.createdAt = notificationModel.getCreatedAt();
    this.updatedAt = notificationModel.getUpdatedAt();
  }
}
