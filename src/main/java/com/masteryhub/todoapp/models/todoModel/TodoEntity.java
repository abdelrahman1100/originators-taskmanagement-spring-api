package com.masteryhub.todoapp.models.todoModel;

import com.masteryhub.todoapp.dtos.todoDto.RequestTodoDto;
import com.masteryhub.todoapp.dtos.userDto.AddFriendToTodoDto;
import java.time.Instant;
import java.util.ArrayList;
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

  private String userId;

  private Long customId;

  private String title;

  private String description;

  private Status status;

  private Permission permission;

  private List<AddFriendToTodoDto> friends = new ArrayList<>();

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
    if (requestTodoDto.getStatus() == null) {
      this.status = Status.TODO;
    }
  }

  public void addFriend(AddFriendToTodoDto friend) {
    this.friends.add(friend);
  }

  public void removeFriend(String friend) {
    for (int i = 0; i < friends.size(); i++) {
      if (friends.get(i).getUsername().equals(friend)) {
        friends.remove(i);
        break;
      }
    }
  }

  public String getFriendByUsername(String username) {
    for (AddFriendToTodoDto friend : friends) {
      if (friend.getUsername().equals(username)) {
        return friend.getUsername();
      }
    }
    return null;
  }

  public void editFriendPermission(AddFriendToTodoDto addFriendToTodoDto) {
    for (AddFriendToTodoDto friend : friends) {
      if (friend.getUsername().equals(addFriendToTodoDto.getUsername())) {
        friend.setPermission(addFriendToTodoDto.getPermission());
        break;
      }
    }
  }
}
