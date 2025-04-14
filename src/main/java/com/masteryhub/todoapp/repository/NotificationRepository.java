package com.masteryhub.todoapp.repository;

import com.masteryhub.todoapp.models.notificationModel.NotificationModel;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<NotificationModel, String> {
  List<NotificationModel> findByRecipient(String recipient);
}
