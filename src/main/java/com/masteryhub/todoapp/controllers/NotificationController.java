package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dtos.notificationDto.NotificationDto;
import com.masteryhub.todoapp.repository.NotificationRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.service.notificationService.NotificationService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {

  private final NotificationRepository notificationRepository;
  private final UserRepository userRepository;
  private final NotificationService notificationService;

  @Autowired
  public NotificationController(
      NotificationRepository notificationRepository,
      UserRepository userRepository,
      NotificationService notificationService) {
    this.notificationRepository = notificationRepository;
    this.userRepository = userRepository;
    this.notificationService = notificationService;
  }

  @GetMapping
  public List<NotificationDto> getUserNotifications() {
    return notificationService.getUserNotifications();
  }

  @PostMapping("/read/{id}")
  public void markAsRead(@PathVariable String id) {
    notificationService.markAsRead(id);
  }
}
