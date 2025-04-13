package com.masteryhub.todoapp.service.notificationService;

import com.masteryhub.todoapp.dtos.notificationDto.NotificationDto;
import com.masteryhub.todoapp.models.notificationModel.NotificationModel;
import com.masteryhub.todoapp.models.notificationModel.NotificationType;
import com.masteryhub.todoapp.models.userModel.UserEntity;
import com.masteryhub.todoapp.repository.NotificationRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final SimpMessagingTemplate messagingTemplate;

  @Autowired
  public NotificationService(
      NotificationRepository notificationRepository,
      UserRepository userRepository,
      SimpMessagingTemplate messagingTemplate) {
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
    this.messagingTemplate = messagingTemplate;
  }

  public void sendNotification(
      String recipientUsername, String senderUsername, NotificationType type, String message) {
    UserEntity recipient = userRepository.findByUsername(recipientUsername).orElse(null);
    UserEntity sender = userRepository.findByUsername(senderUsername).orElse(null);

    if (recipient == null || sender == null) return;

    NotificationModel notification = new NotificationModel();
    notification.setRecipient(recipient.getId());
    notification.setSender(sender.getId());
    notification.setType(type);
    notification.setMessage(message);
    notification.setIsRead(false);
    notificationRepository.save(notification);
    messagingTemplate.convertAndSend(
        "/send/notifications/" + recipient.getUsername(), new NotificationDto(notification));
  }

  public List<NotificationDto> getUserNotifications() {
    String username =
        ((UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getUsername();
    UserEntity user = userRepository.findByUsername(username).orElseThrow();
    return notificationRepository.findByRecipient(user.getId()).stream()
        .map(NotificationDto::new)
        .toList();
  }

  public void markAsRead(String id) {
    NotificationModel notification = notificationRepository.findById(id).orElseThrow();
    notification.setIsRead(true);
    notificationRepository.save(notification);
  }
}
