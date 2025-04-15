package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dtos.messageDto.MessageDto;
import com.masteryhub.todoapp.dtos.todoDto.RequestTodoDto;
import com.masteryhub.todoapp.dtos.todoDto.ResponseTodoDto;
import com.masteryhub.todoapp.dtos.userDto.*;
import com.masteryhub.todoapp.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired UserService userService;

  @GetMapping("/{username}/friend")
  public List<ResponseFriendsDto> getAllFriends(@PathVariable String username) {
    return userService.getAllFriends(username);
  }

  @PostMapping("/{username}/friend")
  public ResponseEntity<MessageDto> addFriend(@RequestBody RequestFriendDto friendsDto) {
    return userService.addFriend(friendsDto);
  }

  @DeleteMapping("/{username}/friend/{friendUsername}")
  public ResponseEntity<MessageDto> removeFriend(@RequestBody RequestFriendDto friendsDto) {
    return userService.removeFriend(friendsDto);
  }

  @PatchMapping("/{username}/settings")
  public ResponseEntity<MessageDto> editSettings(
      @RequestBody SettingsDto settingsDto, @PathVariable String username) {
    return userService.editSettings(settingsDto, username);
  }

  @GetMapping("/{username}/settings")
  public ResponseEntity<UserSettingsDto> getUserSettings(@PathVariable String username) {
    return userService.getUserSettings(username);
  }

  @PatchMapping("/{username}/profile")
  public ResponseEntity<MessageDto> editProfile(
      @RequestBody EditProfileDto editProfileDto, @PathVariable String username) {
    return userService.editProfile(editProfileDto, username);
  }

  @PostMapping("/add/friend/todo/{id}")
  public ResponseEntity<MessageDto> addFriendToTodo(
      @RequestBody AddFriendToTodoDto addFriendToTodoDto, @PathVariable Long id) {
    return userService.addFriendToTodo(addFriendToTodoDto, id);
  }

  @DeleteMapping("/remove/friend/todo/{id}")
  public ResponseEntity<MessageDto> removeFriendFromTodo(
      @RequestBody RemoveFriendDto removeFriendDto, @PathVariable Long id) {
    return userService.removeFriendFromTodo(removeFriendDto, id);
  }

  @GetMapping("/shared/todos")
  public ResponseEntity<List<ResponseTodoDto>> getSharedTodos() {
    return userService.getSharedTodos();
  }

  @GetMapping("/todo/friends/{id}")
  public ResponseEntity<List<ResponseFriendsDto>> getSharedTodoFriends(@PathVariable Long id) {
    return userService.getSharedTodoFriends(id);
  }

  @PatchMapping("/edit/friend/permission/{id}")
  public ResponseEntity<MessageDto> editFriendPermission(
      @RequestBody AddFriendToTodoDto addFriendToTodoDto, @PathVariable Long id) {
    return userService.editFriendPermission(addFriendToTodoDto, id);
  }

  @PatchMapping("/edit/sharedtodo/{id}")
  public ResponseEntity<MessageDto> editSharedTodo(
      @RequestBody RequestTodoDto requestTodoDto, @PathVariable Long id) {
    return userService.editSharedTodo(requestTodoDto, id);
  }

  @PostMapping("/{username}/profile-image")
  public ResponseEntity<MessageDto> uploadImage(
      @RequestBody ProfileImageDto profileImageDto, @PathVariable String username) {
    return userService.uploadImage(profileImageDto, username);
  }

  @GetMapping("/{username}/profile-image")
  public ResponseEntity<ProfileImageDto> getImage(@PathVariable String username) {
    return userService.getImage(username);
  }

  @DeleteMapping("/profile/{username}/settings/profile-image")
  public ResponseEntity<MessageDto> deleteImage(@PathVariable String username) {
    return userService.deleteImage(username);
  }
}
