package com.dbtest.yashwith.core_auth.service.auth;

import com.dbtest.yashwith.model.auth.AuthRequest;
import com.dbtest.yashwith.model.auth.UserCreateRequest;
import com.dbtest.yashwith.response.ApiResponse;

public interface AuthService {
    public ApiResponse createUser(UserCreateRequest userCreateRequest);

    public ApiResponse loginUser(AuthRequest loginRequest);
}
