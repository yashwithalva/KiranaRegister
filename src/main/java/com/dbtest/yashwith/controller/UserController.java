package com.dbtest.yashwith.controller;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.model.user.LoginRequest;
import com.dbtest.yashwith.model.user.UserProfile;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.service.UserService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Get all the users present
     * @return JSON object containing list of users.
     */
    @GetMapping("/")
    public ResponseEntity<ApiResponse> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get user profile by TokenId.
     * @return userDTO
     */
    @GetMapping("/profile")
    public ResponseEntity<ApiResponse> getUserInfo(@RequestHeader("Authorization") String token) {
        return ResponseEntity.ok(userService.getUserProfile(token));
    }


    /**
     * Delete user using userId
     * @param userId of the user to be deleted.~
     * @return
     */
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleteUser(@RequestBody String userId){
        return ResponseEntity.ok(userService.deleteUser(userId));
    }

    /**
     * Update user by id.
     * @param userId Generated id for each user
     * @return
     */
    @PostMapping("/update")
    public ResponseEntity<?> updateUserById(@RequestParam String userId) {
        return ResponseEntity.badRequest().build();
    }

    /**
     * Update password using userId
     *
     * @param userId id of the current authorized user.
     * @return Returns 200 if successfully.
     */
    @PostMapping("/update/password")
    public ResponseEntity<?> updateUserPassword(@RequestParam String userId) {
        return ResponseEntity.badRequest().build();
    }
}
