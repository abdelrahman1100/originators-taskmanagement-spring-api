package com.masteryhub.todoapp.controllers;

import com.masteryhub.todoapp.dto.*;
import com.masteryhub.todoapp.service.TodoFriendsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos/friends")
public class TodoFriendsController {

  TodoFriendsService todoFriendsService;

  @Autowired
  public TodoFriendsController(TodoFriendsService todoFriendsService) {
    this.todoFriendsService = todoFriendsService;
  }

  @PostMapping("/add-friend-to-todo")
  public ResponseEntity<MessageDto> addFriendToTodo(
      @RequestBody AddFriendDto addFriendDto, @RequestParam Long id) {
    return todoFriendsService.addFriendToTodo(addFriendDto, id);
  }

  @DeleteMapping("/remove-friend-from-todo")
  public ResponseEntity<MessageDto> removeFriendFromTodo(
      @RequestBody AddFriendDto addFriendDto, @RequestParam Long id) {
    return todoFriendsService.removeFriendFromTodo(addFriendDto, id);
  }

  @GetMapping("/get-shared-todos")
  public ResponseEntity<List<ResponseTodoDto>> getSharedTodos() {
    return todoFriendsService.getSharedTodos();
  }

  @GetMapping("/get-friends-for-specific-todo")
  public ResponseEntity<List<FriendsForSharedTodoDto>> getFriendsForSpecificTodo(
      @RequestParam Long id) {
    return todoFriendsService.getFriendsForSpecificTodo(id);
  }

  //    @PutMapping("update-sharedtodo")
  //    public ResponseEntity<MessageDto> updateSharedTodo(@RequestBody RequestTodoDto
  // requestTodoDto) {
  //        return todoFriendsService.updateSharedTodo(requestTodoDto);
  //    }

  @PutMapping("update-sharedtodo-permission")
  public ResponseEntity<MessageDto> updateSharedTodoPermission(
      @RequestBody AddFriendDto addFriendDto, @RequestParam Long id) {
    return todoFriendsService.updateSharedTodoPermission(addFriendDto, id);
  }
}
