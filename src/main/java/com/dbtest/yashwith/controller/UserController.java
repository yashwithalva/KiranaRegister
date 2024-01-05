package com.dbtest.yashwith.controller;

import com.dbtest.yashwith.entities.User;
import com.dbtest.yashwith.request.Wip_usernamePassword;
import com.dbtest.yashwith.response.Wip_responseLogin;
import com.dbtest.yashwith.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.hibernate.engine.jdbc.Size.length;

@RestController
@RequestMapping("/user")
public class UserController {

    private UserService userService;

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
     * Create New User
     * @param user - User object is required.
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity<String> createNewUser(@RequestBody User user){
        User createdUser = userService.createUser(user);
        if(createdUser != null){
            String userId = createdUser.getId();
            return ResponseEntity.status(HttpStatus.CREATED).body(userId);
        }
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body("Could not create user");
    }

    @PostMapping("/login")
    public ResponseEntity<Wip_responseLogin> loginUser(@RequestBody Wip_usernamePassword requestLogin){
        Wip_responseLogin responseLogin = userService.userLogin(requestLogin.getEmail(), requestLogin.getPassword());
        if(responseLogin == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.status(HttpStatus.OK).body(responseLogin);
    }

}
