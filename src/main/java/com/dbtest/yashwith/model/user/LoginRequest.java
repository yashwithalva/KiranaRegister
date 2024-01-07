package com.dbtest.yashwith.model.user;

import lombok.Data;

@Data
public class LoginRequest {
    String email;
    String password;
}
