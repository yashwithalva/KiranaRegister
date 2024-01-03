package com.dbtest.yashwith.controller;

import com.dbtest.yashwith.model.User;
import com.dbtest.yashwith.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User createdUser = userService.createUser(user);
        System.out.println("Post mapping introduced! " + createdUser);
        return ResponseEntity.ok(createdUser);
    }

    @GetMapping("/description")
    public ResponseEntity<User> getUserDescription(@RequestParam String username, @RequestParam String password){
        User user = userService.getUserDescription(username, password);
        System.out.println("Get mapping received :=> username: " + username + ", password: " + password);
        System.out.println("Received User: " + user);
        if(user != null) return ResponseEntity.ok(user);
        else return ResponseEntity.notFound().build();
    }

}
