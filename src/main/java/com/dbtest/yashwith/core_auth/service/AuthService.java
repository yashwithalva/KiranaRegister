package com.dbtest.yashwith.core_auth.service;

import com.dbtest.yashwith.model.auth.AuthRequest;
import com.dbtest.yashwith.model.auth.UserCreateRequest;
import com.dbtest.yashwith.response.ApiResponse;
import com.dbtest.yashwith.response.AuthResponse;

public interface AuthService {
    public AuthResponse createUser(UserCreateRequest userCreateRequest);

    public ApiResponse loginUser(AuthRequest loginRequest);
}
