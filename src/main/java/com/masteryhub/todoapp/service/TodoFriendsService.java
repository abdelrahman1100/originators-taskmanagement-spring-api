package com.masteryhub.todoapp.service;

import com.masteryhub.todoapp.dto.*;
import com.masteryhub.todoapp.models.SharedTodosEntity;
import com.masteryhub.todoapp.models.TodoEntity;
import com.masteryhub.todoapp.models.UserEntity;
import com.masteryhub.todoapp.repository.SharedTodoRepository;
import com.masteryhub.todoapp.repository.TodoRepository;
import com.masteryhub.todoapp.repository.UserRepository;
import com.masteryhub.todoapp.security.UserDetailsImpl;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class TodoFriendsService {

  SharedTodoRepository sharedTodoRepository;
  UserRepository userRepository;
  TodoRepository todoRepository;

  @Autowired
  public TodoFriendsService(
      SharedTodoRepository sharedTodoRepository,
      UserRepository userRepository,
      TodoRepository todoRepository) {
    this.sharedTodoRepository = sharedTodoRepository;
    this.userRepository = userRepository;
    this.todoRepository = todoRepository;
  }

  public ResponseEntity<MessageDto> addFriendToTodo(AddFriendDto addFriendDto, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    Optional<TodoEntity> todoOptional =
        todoRepository.findByUserIdAndCustomIdAndDeletedAtIsNull(user.get().getId(), id);
    if (todoOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    TodoEntity todo = todoOptional.get();
    Optional<UserEntity> friend = userRepository.findByUsername(addFriendDto.getUsername());
    if (friend.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    SharedTodosEntity sharedTodo = new SharedTodosEntity();
    sharedTodo.setTodo(todo.getId());
    sharedTodo.setFriend(friend.get().getId());
    sharedTodo.setPermission(addFriendDto.getPermission());
    sharedTodoRepository.save(sharedTodo);
    return ResponseEntity.ok(new MessageDto("Friend added to todo"));
  }

  public ResponseEntity<MessageDto> removeFriendFromTodo(AddFriendDto addFriendDto, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    Optional<TodoEntity> todoOptional =
        todoRepository.findByUserIdAndCustomIdAndDeletedAtIsNull(user.get().getId(), id);
    if (todoOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    TodoEntity todo = todoOptional.get();
    Optional<UserEntity> friend = userRepository.findByUsername(addFriendDto.getUsername());
    if (friend.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Optional<SharedTodosEntity> sharedTodoOptional =
        sharedTodoRepository.findByTodoAndFriend(todo.getId(), friend.get().getId());
    if (sharedTodoOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    sharedTodoRepository.delete(sharedTodoOptional.get());
    return ResponseEntity.ok(new MessageDto("Friend removed from todo"));
  }

  public ResponseEntity<List<ResponseTodoDto>> getSharedTodos() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    List<SharedTodosEntity> sharedTodosEntities =
        sharedTodoRepository.findByFriend(user.get().getId());
    List<String> sharedTodos =
        sharedTodosEntities.stream().map(SharedTodosEntity::getTodo).collect(Collectors.toList());
    List<TodoEntity> todos = todoRepository.findAllById(sharedTodos);
    List<ResponseTodoDto> responseTodoDtos =
        todos.stream().map(ResponseTodoDto::from).collect(Collectors.toList());
    return ResponseEntity.ok(responseTodoDtos);
  }

  public ResponseEntity<List<FriendsForSharedTodoDto>> getFriendsForSpecificTodo(Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    Optional<TodoEntity> todoOptional =
        todoRepository.findByUserIdAndCustomIdAndDeletedAtIsNull(user.get().getId(), id);
    if (todoOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    TodoEntity todo = todoOptional.get();
    List<SharedTodosEntity> sharedTodosEntities = sharedTodoRepository.findByTodo(todo.getId());
    List<FriendsForSharedTodoDto> friends =
        sharedTodosEntities.stream()
            .map(
                sharedTodo ->
                    userRepository
                        .findById(sharedTodo.getFriend())
                        .map(
                            userr ->
                                new FriendsForSharedTodoDto(
                                    userr.getUsername(), sharedTodo.getPermission()))
                        .orElse(null))
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    return ResponseEntity.ok(friends);
  }

  public ResponseEntity<MessageDto> updateSharedTodoPermission(AddFriendDto addFriendDto, Long id) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    String email = userDetails.getEmail();
    Optional<UserEntity> user = userRepository.findByEmail(email);
    Optional<TodoEntity> todoOptional =
        todoRepository.findByUserIdAndCustomIdAndDeletedAtIsNull(user.get().getId(), id);
    if (todoOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    TodoEntity todo = todoOptional.get();
    Optional<UserEntity> friend = userRepository.findByUsername(addFriendDto.getUsername());
    if (friend.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    Optional<SharedTodosEntity> sharedTodoOptional =
        sharedTodoRepository.findByTodoAndFriend(todo.getId(), friend.get().getId());
    if (sharedTodoOptional.isEmpty()) {
      return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
    SharedTodosEntity sharedTodo = sharedTodoOptional.get();
    sharedTodo.setPermission(addFriendDto.getPermission());
    sharedTodoRepository.save(sharedTodo);
    return ResponseEntity.ok(new MessageDto("Shared todo permission updated"));
  }

  //    public ResponseEntity<MessageDto> updateSharedTodo(RequestTodoDto requestTodoDto) {
  //        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
  //        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
  //        String email = userDetails.getEmail();
  //        Optional<UserEntity> user = userRepository.findByEmail(email);
  //        Optional<TodoEntity> todoOptional =
  //                todoRepository.findByUserIdAndCustomIdAndDeletedAtIsNull(user.get().getId(),
  // requestTodoDto.getId());
  //        if (todoOptional.isEmpty()) {
  //            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  //        }
  //        TodoEntity todo = todoOptional.get();
  //        Optional<SharedTodosEntity> sharedTodoOptional =
  //                sharedTodoRepository.findByTodoAndFriend(todo.getId(), user.get().getId());
  //        if (sharedTodoOptional.isEmpty()) {
  //            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
  //        }
  //        SharedTodosEntity sharedTodo = sharedTodoOptional.get();
  //        sharedTodo.setPermission(requestTodoDto.getPermission());
  //        sharedTodoRepository.save(sharedTodo);
  //        return ResponseEntity.ok(new MessageDto("Shared todo updated"));
  //    }
}
