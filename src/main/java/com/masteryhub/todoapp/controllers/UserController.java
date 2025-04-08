package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.*;
import com.masteryhub.todoapp.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

  @Autowired UserService userService;

  @GetMapping("/get-all-friends")
  public List<ResponseFriendsDto> getAllFriends() {
    return userService.getAllFriends();
  }

  @PatchMapping("/add-friend")
  public ResponseEntity<MessageDto> addFriend(@RequestBody RequestFriendsDto friendsDto) {
    return userService.addFriend(friendsDto);
  }

  @PatchMapping("/remove-friend")
  public ResponseEntity<MessageDto> removeFriend(@RequestBody RequestFriendsDto friendsDto) {
    return userService.removeFriend(friendsDto);
  }

  @PatchMapping("/edit-displaymode")
  public ResponseEntity<MessageDto> editDisplayMode(@RequestBody DisplayModeDto displayModeDto) {
    return userService.editDisplayMode(displayModeDto);
  }

  @PatchMapping("/edit-theme")
  public ResponseEntity<MessageDto> editTheme(@RequestBody PrimaryColorDto primaryColorDto) {
    return userService.editTheme(primaryColorDto);
  }

  @GetMapping("/settings")
  public ResponseEntity<UserSettingsDto> getUserSettings() {
    return userService.getUserSettings();
  }

  @PatchMapping("/edit-profile")
  public ResponseEntity<MessageDto> editProfile(@RequestBody EditProfileDto editProfileDto) {
    return userService.editProfile(editProfileDto);
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

  @PostMapping("/upload-image")
  public ResponseEntity<MessageDto> uploadImage(@RequestBody ProfileImageDto profileImageDto) {
    return userService.uploadImage(profileImageDto);
  }

  @GetMapping("/get-image")
  public ResponseEntity<ProfileImageDto> getImage() {
    return userService.getImage();
  }
}
