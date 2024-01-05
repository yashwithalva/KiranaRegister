package com.dbtest.yashwith.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePassword {
    private String oldpassword;
    private String newpassword;
}
