package com.dbtest.yashwith.model;

import com.dbtest.yashwith.enums.Role;
import lombok.Data;

@Data
public class UpdateUserRole {
    private String id;
    private Role role;
}
