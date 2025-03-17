package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.MessageDto;
import com.masteryhub.todoapp.dto.RequestFriendsDto;
import com.masteryhub.todoapp.dto.ResponseFriendsDto;
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
}
