package com.dbtest.yashwith.controller;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.model.LoginRequest;
import com.dbtest.yashwith.model.UpdatePassword;
import com.dbtest.yashwith.model.UpdateUserInfo;
import com.dbtest.yashwith.model.UserDTO;
import com.dbtest.yashwith.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Slf4j
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
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if(users.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }


    /**
     * Create a New User
     * @param user - User object is required.
     * @return id of the user created.
     */
    @PostMapping("/create")
    public ResponseEntity<String> createNewUser(@RequestBody User user){
        User createdUser = userService.createUser(user);
        if(createdUser != null){
            String userId = createdUser.getId();
            return ResponseEntity.status(HttpStatus.CREATED).body(userId);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Could not create user");
    }


    /**
     * Login using email and password.
     * @param loginRequest consist of username and password.
     * @return UserDTO if successfully logged in.
     */
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        if (!isLoginRequestValid(loginRequest)) {
            return ResponseEntity.badRequest().build();
        }
        User login = userService.userLogin(loginRequest);
        if (login == null) {
            return ResponseEntity.badRequest().build();
        }

        // TODO: Convert logged in user to user_dto

        return ResponseEntity.ok().body(login);
    }


    /**
     * Get user information by userId.
     * @param userId -> Get UserId.
     * @return userDTO
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserInfo(@PathVariable String userId){
        // TODO: Convert user to USER DTO.
        log.error("User Id: " + userId);

        User userInfo = userService.getUserInformationById(userId).orElse(null);

        UserDTO userDTO = new UserDTO();


        if(userInfo != null){
            return ResponseEntity.ok().body(userInfo);
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * Update user by id.
     * @param userId Generated id for each user
     * @param updateUserInfo DTO consisting fields that could be updated.
     * @return
     */
    @PostMapping("/{userId}/update")
    public ResponseEntity<?> updateUserById(@RequestParam String userId, @RequestBody UpdateUserInfo updateUserInfo){
        if (updateUserInfo == null) return ResponseEntity.badRequest().build();
        if (userService.updateUserInfomation(userId, updateUserInfo)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * Update password using userId
     * @param userId  id of the current authorized user.
     * @param updatePassword old and new password.
     * @return Returns 200 if successfull.
     */
    @PostMapping("/{userId}/password")
    public ResponseEntity<?> updateUserPassword(@RequestParam String userId, @RequestBody UpdatePassword updatePassword){
        if(updatePassword == null || updatePassword.getNewpassword() == null || updatePassword.getOldpassword() == null)
            return ResponseEntity.badRequest().build();

        if(userService.updateUserPassword(userId, updatePassword)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }


    /**
     * Utils function
     * @param loginRequest DTO consist of username and password
     * @return boolean Request format if correct.
     */
    private boolean isLoginRequestValid(LoginRequest loginRequest){
        return loginRequest != null
                && loginRequest.getPassword() != null
                && loginRequest.getEmail() != null;
    }

}
