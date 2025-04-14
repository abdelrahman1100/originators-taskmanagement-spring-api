package com.masteryhub.todoapp.service.notificationService;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationEventListener {

  private final NotificationService notificationService;

  public NotificationEventListener(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @EventListener
  public void handleNotificationEvent(NotificationEvent event) {
    notificationService.sendNotification(
        event.getRecipientUsername(),
        event.getSenderUsername(),
        event.getType(),
        event.getMessage());
  }
}
