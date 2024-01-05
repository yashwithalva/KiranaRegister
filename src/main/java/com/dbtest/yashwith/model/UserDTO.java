package com.dbtest.yashwith.model;

import com.dbtest.yashwith.enums.Role;
import lombok.Data;

@Data
public class UserDTO {
    private String id;
    private String name;
    private String email;
    private String phone;
    private String age;
    private Role role;
}
