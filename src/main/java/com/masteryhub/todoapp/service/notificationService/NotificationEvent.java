package com.masteryhub.todoapp.service.notificationService;

import com.masteryhub.todoapp.models.notificationModel.NotificationType;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class NotificationEvent extends ApplicationEvent {

  private final String senderUsername;
  private final String recipientUsername;
  private final NotificationType type;
  private final String message;

  public NotificationEvent(
      Object source,
      String senderUsername,
      String recipientUsername,
      NotificationType type,
      String message) {
    super(source);
    this.senderUsername = senderUsername;
    this.recipientUsername = recipientUsername;
    this.type = type;
    this.message = message;
  }
}
