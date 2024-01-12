package com.dbtest.yashwith.controller.auth;

import com.dbtest.yashwith.core_auth.service.AuthServiceImpl;
import com.dbtest.yashwith.model.auth.AuthRequest;
import com.dbtest.yashwith.model.auth.UserCreateRequest;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.response.AuthResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.checkerframework.checker.units.qual.A;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthServiceImpl authService;

    @PostMapping("/create")
    public ResponseEntity<AuthResponse> createUser(@RequestBody UserCreateRequest request) {
        AuthResponse authResponse = authService.createUser(request);
        return ResponseEntity.ok().body(authResponse);
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> loginUser(@RequestBody AuthRequest request) {
        return ResponseEntity.ok().body(authService.loginUser(request));
    }
}
