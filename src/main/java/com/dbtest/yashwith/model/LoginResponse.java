package com.dbtest.yashwith.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String id;
    private String name;
    private String email;
    private String phoneNo;
    private int age;
    private String role;
}
