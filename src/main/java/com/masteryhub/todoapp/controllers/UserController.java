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

    @Autowired
    UserService userService;

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

//    /user GET
//    /user/{id} GET
//    /user/{id} DELETE
//    /user/{id} PATCH
    // /USER/{id}/edit
//    /user/{id}/profile/edit
    // /products/{id}/profile/image/upload/{id}/image/asd/asd /asd/asd/asd
    // /customers/{id}/profile/edit
//    /user/{id}/todos GET
//    /user/{id}/todos/{todoId} GET
//    /user/{id}/todos/{todoId} DELETE
}
