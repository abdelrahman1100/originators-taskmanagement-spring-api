package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.messageDto.MessageDto;
import com.masteryhub.todoapp.dto.todoDto.RequestTodoDto;
import com.masteryhub.todoapp.dto.todoDto.ResponseTodoDto;
import com.masteryhub.todoapp.dto.userDto.*;
import com.masteryhub.todoapp.handlers.ErrorMessageHandler;
import com.masteryhub.todoapp.models.todoModel.TodoEntity;
import com.masteryhub.todoapp.models.userModel.ProfileImage;
import com.masteryhub.todoapp.models.userModel.UserEntity;
import com.masteryhub.todoapp.repository.TodoRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.UserDetailsImpl;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  UserRepository userRepository;
  TodoRepository todoRepository;

  @Autowired
  public UserService(UserRepository userRepository, TodoRepository todoRepository) {
    this.userRepository = userRepository;
    this.todoRepository = todoRepository;
  }

  public List<ResponseFriendsDto> getAllFriends() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return Collections.emptyList();
    }
    List<ResponseFriendsDto> responseFriendsDtos =
        user.get().getFriends().stream()
            .map(
                username ->
                    userRepository
                        .findByUsername(username)
                        .map(friend -> new ResponseFriendsDto(friend.getUsername()))
                        .orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    return ResponseEntity.ok(responseFriendsDtos).getBody();
  }

  public ResponseEntity<MessageDto> addFriend(RequestFriendsDto friendsDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (userRepository.findByUsername(friendsDto.getUsername()).isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    if (user.get().getFriends().contains(friendsDto.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.Todo.get_user_already_friend()));
    }
    user.get().addFriend(friendsDto.getUsername());
    userRepository.save(user.get());
    return ResponseEntity.ok(new MessageDto("Friend added"));
  }

  public ResponseEntity<MessageDto> removeFriend(RequestFriendsDto friendsDto) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (userRepository.findByUsername(friendsDto.getUsername()).isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    if (!user.get().getFriends().contains(friendsDto.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageDto("Friend doesn't exists"));
    }
    user.get().removeFriend(friendsDto.getUsername());
    userRepository.save(user.get());
    return ResponseEntity.ok(new MessageDto("Friend removed"));
  }

  public ResponseEntity<MessageDto> editSettings(SettingsDto settingsDto, String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty() || !username.equals(userDetails.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    user.get().getSettings().getTheme().setPrimaryColor(settingsDto.getTheme().getPrimaryColor());
    userRepository.save(user.get());
    return ResponseEntity.ok(new MessageDto("Settings Updated"));
  }

  public ResponseEntity<UserSettingsDto> getUserSettings(String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty() || !username.equals(userDetails.getUsername())) {
      return ResponseEntity.badRequest().body(new UserSettingsDto());
    }
    UserSettingsDto userSettingsDto = new UserSettingsDto();
    userSettingsDto.setSettings(user.get().getSettings());
    return ResponseEntity.ok(userSettingsDto);
  }

  public ResponseEntity<MessageDto> editProfile(EditProfileDto editProfileDto, String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty() || !username.equals(userDetails.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    UserEntity userEntity = user.get();
    userEntity.setFullName(editProfileDto.getFullName());
    userEntity.setPhoneNumber(editProfileDto.getPhoneNumber());
    userEntity.setAddress(editProfileDto.getAddress());
    userRepository.save(userEntity);
    return ResponseEntity.ok(new MessageDto("Profile Updated"));
  }

  public ResponseEntity<MessageDto> addFriendToTodo(
      AddFriendToTodoDto addFriendToTodoDto, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    Optional<TodoEntity> todo = todoRepository.findByUserIdAndCustomId(user.get().getId(), id);
    if (todo.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.Todo.get_todo_not_found()));
    }
    if (todo.get().getFriendByUsername(addFriendToTodoDto.getUsername()) != null) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.Todo.get_user_already_friend()));
    }
    if (userRepository.findByUsername(addFriendToTodoDto.getUsername()).isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    if (!user.get().getFriends().contains(addFriendToTodoDto.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.Todo.get_user_already_friend()));
    }
    TodoEntity todoEntity = todo.get();
    todoEntity.addFriend(addFriendToTodoDto);
    todoRepository.save(todoEntity);
    return ResponseEntity.ok(new MessageDto("Friend added to todo"));
  }

  public ResponseEntity<MessageDto> removeFriendFromTodo(RemoveFriendDto removeFriendDto, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    Optional<TodoEntity> todo = todoRepository.findByUserIdAndCustomId(user.get().getId(), id);
    if (todo.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.Todo.get_todo_not_found()));
    }
    TodoEntity todoEntity = todo.get();
    if (todoEntity.getFriendByUsername(removeFriendDto.getUsername()) == null) {
      return ResponseEntity.badRequest().body(new MessageDto("Friend doesn't exists"));
    }
    if (userRepository.findByUsername(removeFriendDto.getUsername()).isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    if (!user.get().getFriends().contains(removeFriendDto.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageDto("Friend doesn't exists"));
    }
    todoEntity.removeFriend(removeFriendDto.getUsername());
    todoRepository.save(todoEntity);
    return ResponseEntity.ok(new MessageDto("Friend removed from todo"));
  }

  public ResponseEntity<List<ResponseTodoDto>> getSharedTodos() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest().body(Collections.emptyList());
    }
    List<TodoEntity> todos = todoRepository.findByFriendsUsername(user.get().getUsername());
    List<ResponseTodoDto> responseTodoDtos = todos.stream().map(ResponseTodoDto::from).toList();
    return ResponseEntity.ok(responseTodoDtos);
  }

  public ResponseEntity<List<ResponseFriendsDto>> getSharedTodoFriends(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest().body(Collections.emptyList());
    }
    Optional<TodoEntity> todo = todoRepository.findByUserIdAndCustomId(user.get().getId(), id);
    if (todo.isEmpty()) {
      return ResponseEntity.badRequest().body(Collections.emptyList());
    }
    List<ResponseFriendsDto> responseFriendsDtos = new ArrayList<>();
    for (AddFriendToTodoDto friend : todo.get().getFriends()) {
      responseFriendsDtos.add(new ResponseFriendsDto(friend.getUsername()));
    }
    return ResponseEntity.ok(responseFriendsDtos);
  }

  public ResponseEntity<MessageDto> editFriendPermission(
      AddFriendToTodoDto addFriendToTodoDto, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    Optional<TodoEntity> todo = todoRepository.findByUserIdAndCustomId(user.get().getId(), id);
    if (todo.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.Todo.get_todo_not_found()));
    }
    TodoEntity todoEntity = todo.get();
    if (todoEntity.getFriendByUsername(addFriendToTodoDto.getUsername()) == null) {
      return ResponseEntity.badRequest().body(new MessageDto("Friend doesn't exists"));
    }
    if (userRepository.findByUsername(addFriendToTodoDto.getUsername()).isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    if (!user.get().getFriends().contains(addFriendToTodoDto.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageDto("Friend doesn't exists"));
    }
    todoEntity.editFriendPermission(addFriendToTodoDto);
    todoRepository.save(todoEntity);
    return ResponseEntity.ok(new MessageDto("Friend permission updated"));
  }

  public ResponseEntity<MessageDto> editSharedTodo(RequestTodoDto requestTodoDto, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty()) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    TodoEntity todo = todoRepository.findByFriendsUsernameAndCustomId(user.get().getUsername(), id);
    if (todo == null) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.Todo.get_todo_not_found()));
    }
    TodoEntity todoEntity = todo;
    todoEntity.setTitle(requestTodoDto.getTitle());
    todoEntity.setDescription(requestTodoDto.getDescription());
    todoEntity.setDueDate(requestTodoDto.getDueDate());
    todoEntity.setStatus(requestTodoDto.getStatus());
    todoRepository.save(todoEntity);
    return ResponseEntity.ok(new MessageDto("Shared Todo updated"));
  }

  public ResponseEntity<MessageDto> uploadImage(ProfileImageDto profileImageDto, String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty() || !username.equals(userDetails.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    if (!profileImageDto.isValidImageType()) {
      return ResponseEntity.badRequest().body(new MessageDto("Invalid image type"));
    }
    byte[] imageData = profileImageDto.getImageData();

    ProfileImage profileImage = new ProfileImage();
    profileImage.setImageData(imageData);
    profileImage.setImageType(profileImageDto.getImageType());

    user.get().setProfileImage(profileImage);
    userRepository.save(user.get());
    return ResponseEntity.ok(new MessageDto("Image uploaded successfully"));
  }

  public ResponseEntity<ProfileImageDto> getImage(String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();

    Optional<UserEntity> user = userRepository.findByEmail(email);

    if (user.isEmpty() || !username.equals(userDetails.getUsername())) {
      return ResponseEntity.badRequest().build();
    }

    ProfileImage profileImage = user.get().getProfileImage();

    if (profileImage == null || profileImage.getImageData() == null) {
      return ResponseEntity.notFound().build();
    }

    byte[] imageData = profileImage.getImageData();

    String base64Image = "data:image/jpeg;base64," + Base64.getEncoder().encodeToString(imageData);

    ProfileImageDto profileImageDto = new ProfileImageDto();
    profileImageDto.setImageBase64(base64Image);
    profileImageDto.setImageType(profileImage.getImageType());

    return ResponseEntity.ok(profileImageDto);
  }

  public ResponseEntity<MessageDto> deleteImage(String username) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if (user.isEmpty() || !username.equals(userDetails.getUsername())) {
      return ResponseEntity.badRequest()
          .body(new MessageDto(ErrorMessageHandler.User.get_user_not_found()));
    }
    ProfileImage profileImage = new ProfileImage();
    user.get().setProfileImage(profileImage);
    userRepository.save(user.get());
    return ResponseEntity.ok(new MessageDto("Image deleted successfully"));
  }
}
